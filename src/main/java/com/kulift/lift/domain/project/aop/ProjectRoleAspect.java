package com.kulift.lift.domain.project.aop;

import java.util.Map;
import java.util.Optional;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.kulift.lift.domain.project.entity.ProjectRole;
import com.kulift.lift.domain.project.service.ProjectRoleService;
import com.kulift.lift.global.exception.CustomException;
import com.kulift.lift.global.exception.ErrorCode;
import com.kulift.lift.global.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class ProjectRoleAspect {

	private final ProjectRoleService projectRoleService;

	@Before("@annotation(requireProjectRole)")
	public void checkProjectRole(JoinPoint jp, RequireProjectRole requireProjectRole) {
		HttpServletRequest request =
			((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes())
				.getRequest();

		@SuppressWarnings("unchecked")
		Map<String, String> pathVars = (Map<String, String>)
			request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String projectKey = Optional.ofNullable(pathVars)
			.map(vars -> vars.get("projectKey"))
			.orElseThrow(() -> new CustomException(ErrorCode.VALIDATION_ERROR));

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (!(principal instanceof CustomUserDetails user))
			throw new CustomException(ErrorCode.UNAUTHORIZED);

		Long userId = user.getId();
		ProjectRole requiredRole = requireProjectRole.value();

		boolean allowed = switch (requiredRole) {
			case OWNER -> projectRoleService.isOwner(projectKey, userId);
			case MANAGER -> projectRoleService.isManagerOrHigher(projectKey, userId);
			case MEMBER -> projectRoleService.isMember(projectKey, userId);
			default -> throw new CustomException(ErrorCode.FORBIDDEN);
		};

		if (!allowed) {
			throw new CustomException(ErrorCode.FORBIDDEN);
		}
	}
}
