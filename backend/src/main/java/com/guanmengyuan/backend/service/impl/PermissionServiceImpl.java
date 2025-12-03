package com.guanmengyuan.backend.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guanmengyuan.backend.mapper.PermissionMapper;
import com.guanmengyuan.backend.model.constant.TenantConstant;
import com.guanmengyuan.backend.model.domain.Permission;
import com.guanmengyuan.backend.model.enums.CommonStatus;
import com.guanmengyuan.backend.service.PermissionService;
import com.mybatisflex.spring.service.impl.ServiceImpl;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
		implements PermissionService {

	@Transactional
	public void initPermission(String tenantId, Boolean isSys) {
		// 初始化常量路由（只初始化一次，不受租户限制）
		initConstantRoutes();
		// 初始化其他路由数据
		initRoutesFromJson(tenantId, isSys);
	}

	/**
	 * 初始化常量路由（只保存一份，不受租户限制）
	 */
	@Transactional
	private void initConstantRoutes() {

		// 检查常量路由是否已存在
		if (Permission.of()
				.where(Permission::getConstant).eq(true)
				.and(Permission::getTenantId).eq(TenantConstant.CONSTANT_ROUTE_TENANT_ID)
				.exists()) {
			return;
		}

		// 常量路由使用特殊租户ID，表示全局共享
		saveRoute(TenantConstant.CONSTANT_ROUTE_TENANT_ID, null, "login",
				"/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?",
				"layout.blank$view.login", true, "login", "route.login", null, null, true, null,
				null,
				"2", true);

		saveRoute(TenantConstant.CONSTANT_ROUTE_TENANT_ID, null, "403", "/403", "layout.blank$view.403",
				null, "403", "route.403", null, null, true, null, null, "2", true);

		saveRoute(TenantConstant.CONSTANT_ROUTE_TENANT_ID, null, "404", "/404", "layout.blank$view.404",
				null, "404", "route.404", null, null, true, null, null, "2", true);

		saveRoute(TenantConstant.CONSTANT_ROUTE_TENANT_ID, null, "500", "/500", "layout.blank$view.500",
				null, "500", "route.500", null, null, true, null, null, "2", true);
	}

	/**
	 * 从 JSON 数据初始化路由权限
	 */
	@Transactional
	private void initRoutesFromJson(String tenantId, Boolean isSys) {

		// exception 路由组（有 children，menuType="1"）
		Permission exceptionPermission = saveRoute(tenantId, null, "exception", "/exception", "layout.base",
				null, "exception", "route.exception", "ant-design:exception-outlined", 7, null, null,
				null, "1", false);

		saveRoute(tenantId, exceptionPermission.getId(), "exception_403", "/exception/403", "view.403",
				null, "exception_403", "route.exception_403", "ic:baseline-block", null, null, null,
				null, "2", false);
		saveRoute(tenantId, exceptionPermission.getId(), "exception_404", "/exception/404", "view.404",
				null, "exception_404", "route.exception_404", "ic:baseline-web-asset-off", null, null,
				null, null, "2", false);
		saveRoute(tenantId, exceptionPermission.getId(), "exception_500", "/exception/500", "view.500",
				null, "exception_500", "route.exception_500", "ic:baseline-wifi-off", null, null, null,
				null, "2", false);

		// about 路由（无 children，menuType="2"）
		saveRoute(tenantId, null, "about", "/about", "layout.base$view.about",
				null, "about", "route.about", "fluent:book-information-24-regular", 10, null, null,
				null, "2", false);

		// function 路由组（有 children，menuType="1"）
		Permission functionPermission = saveRoute(tenantId, null, "function", "/function", "layout.base",
				null, "function", "route.function", "icon-park-outline:all-application", 6, null, null,
				null, "1", false);

		// function_hide-child 路由组（有 children，menuType="1"）
		Permission hideChildPermission = saveRoute(tenantId, functionPermission.getId(), "function_hide-child",
				"/function/hide-child", null, null, "function_hide-child", "route.function_hide-child",
				"material-symbols:filter-list-off", 2, null, null, null, "1", false);

		saveRoute(tenantId, hideChildPermission.getId(), "function_hide-child_one", "/function/hide-child/one",
				"view.function_hide-child_one", null, "function_hide-child_one",
				"route.function_hide-child_one",
				"material-symbols:filter-list-off", null, true, "function_hide-child", null, "2",
				false);
		saveRoute(tenantId, hideChildPermission.getId(), "function_hide-child_three",
				"/function/hide-child/three",
				"view.function_hide-child_three", null, "function_hide-child_three",
				"route.function_hide-child_three",
				null, null, true, "function_hide-child", null, "2", false);
		saveRoute(tenantId, hideChildPermission.getId(), "function_hide-child_two", "/function/hide-child/two",
				"view.function_hide-child_two", null, "function_hide-child_two",
				"route.function_hide-child_two",
				null, null, true, "function_hide-child", null, "2", false);

		saveRoute(tenantId, functionPermission.getId(), "function_multi-tab", "/function/multi-tab",
				"view.function_multi-tab", null, "function_multi-tab", "route.function_multi-tab",
				"ic:round-tab", null, true, "function_tab", true, "2", false);

		saveRoute(tenantId, functionPermission.getId(), "function_request", "/function/request",
				"view.function_request", null, "function_request", "route.function_request",
				"carbon:network-overlay", 3, null, null, null, "2", false);

		saveRoute(tenantId, functionPermission.getId(), "function_super-page", "/function/super-page",
				"view.function_super-page", null, "function_super-page", "route.function_super-page",
				"ic:round-supervisor-account", 5, null, null, null, "2", false);

		saveRoute(tenantId, functionPermission.getId(), "function_tab", "/function/tab",
				"view.function_tab", null, "function_tab", "route.function_tab",
				"ic:round-tab", 1, null, null, null, "2", false);

		saveRoute(tenantId, functionPermission.getId(), "function_toggle-auth", "/function/toggle-auth",
				"view.function_toggle-auth", null, "function_toggle-auth", "route.function_toggle-auth",
				"ic:round-construction", 4, null, null, null, "2", false);

		// home 路由（无 children，menuType="2"）
		saveRoute(tenantId, null, "home", "/home", "layout.base$view.home",
				null, "home", "route.home", "mdi:monitor-dashboard", 1, null, null, null, "2", false);

		// manage 路由组（有 children，menuType="1"）
		Permission managePermission = saveRoute(tenantId, null, "manage", "/manage", "layout.base",
				null, "manage", "route.manage", "carbon:cloud-service-management", 9, null, null, null,
				"1", false);

		saveRoute(tenantId, managePermission.getId(), "manage_menu", "/manage/menu",
				"view.manage_menu", null, "manage_menu", "route.manage_menu",
				"material-symbols:route", 3, null, null, null, "2", false);

		saveRoute(tenantId, managePermission.getId(), "manage_role", "/manage/role",
				"view.manage_role", null, "manage_role", "route.manage_role",
				"carbon:user-role", 2, null, null, null, "2", false);

		saveRoute(tenantId, managePermission.getId(), "manage_user", "/manage/user",
				"view.manage_user", null, "manage_user", "route.manage_user",
				"ic:round-manage-accounts", 1, null, null, null, "2", false);

		if (isSys) {
			saveRoute(tenantId, managePermission.getId(), "manage_tenant", "/manage/tenant",
					"view.manage_tenant", null, "租户管理", null,
					"lets-icons:group-light", 4, null, null, null, "2", false);
		}

		saveRoute(tenantId, managePermission.getId(), "manage_user-detail", "/manage/user-detail/:id",
				"view.manage_user-detail", true, "manage_user-detail", "route.manage_user-detail",
				null, null, true, "manage_user", null, "2", false);

		// multi-menu 路由组（有 children，menuType="1"）
		Permission multiMenuPermission = saveRoute(tenantId, null, "multi-menu", "/multi-menu", "layout.base",
				null, "multi-menu", "route.multi-menu", null, 8, null, null, null, "1", false);

		Permission multiMenuFirstPermission = saveRoute(tenantId, multiMenuPermission.getId(),
				"multi-menu_first",
				"/multi-menu/first", null, null, "multi-menu_first", "route.multi-menu_first",
				null, 1, null, null, null, "1", false);

		saveRoute(tenantId, multiMenuFirstPermission.getId(), "multi-menu_first_child",
				"/multi-menu/first/child", "view.multi-menu_first_child", null,
				"multi-menu_first_child", "route.multi-menu_first_child", null, null, null, null, null,
				"2", false);

		Permission multiMenuSecondPermission = saveRoute(tenantId, multiMenuPermission.getId(),
				"multi-menu_second",
				"/multi-menu/second", null, null, "multi-menu_second", "route.multi-menu_second",
				null, 2, null, null, null, "1", false);

		Permission multiMenuSecondChildPermission = saveRoute(tenantId, multiMenuSecondPermission.getId(),
				"multi-menu_second_child", "/multi-menu/second/child", null, null,
				"multi-menu_second_child", "route.multi-menu_second_child", null, null, null, null,
				null, "1", false);

		saveRoute(tenantId, multiMenuSecondChildPermission.getId(), "multi-menu_second_child_home",
				"/multi-menu/second/child/home", "view.multi-menu_second_child_home", null,
				"multi-menu_second_child_home", "route.multi-menu_second_child_home", null, null, null,
				null, null, "2", false);

		// user-center 路由（无 children，menuType="2"）
		saveRoute(tenantId, null, "user-center", "/user-center", "layout.base$view.user-center",
				null, "user-center", "route.user-center", null, null, true, null, null, "2", false);
	}

	/**
	 * 保存路由权限
	 * 
	 * @param tenantId   租户ID（常量路由传 null）
	 * @param parentId   父级ID
	 * @param routeName  路由名称
	 * @param routePath  路由路径
	 * @param component  组件
	 * @param props      是否启用props
	 * @param menuName   菜单名称（使用routeName作为默认值）
	 * @param i18nKey    i18n键
	 * @param icon       图标
	 * @param order      排序
	 * @param hideInMenu 是否在菜单中隐藏
	 * @param activeMenu 激活菜单
	 * @param multiTab   是否多标签
	 * @param menuType   菜单类型："1"-目录，"2"-菜单
	 * @param constant   是否为常量路由
	 * @return 保存的权限对象
	 */
	private Permission saveRoute(String tenantId, Long parentId, String routeName, String routePath,
			String component, Boolean props, String menuName, String i18nKey, String icon, Integer order,
			Boolean hideInMenu, String activeMenu, Boolean multiTab, String menuType, Boolean constant) {

		Permission permission = Permission.of()
				.setRouteName(routeName)
				.setRoutePath(routePath)
				.setComponent(component)
				.setMenuName(menuName != null ? menuName : routeName)
				.setMenuType(menuType)
				.setI18nKey(i18nKey)
				.setIcon(icon)
				.setIconType(icon != null ? "1" : null)
				.setOrder(order)
				.setParentId(parentId != null ? parentId : 0L)
				.setHideInMenu(hideInMenu)
				.setActiveMenu(activeMenu)
				.setMultiTab(multiTab)
				.setConstant(constant)
				.setProps(props)
				.setStatus(CommonStatus.ENABLE)
				.setTenantId(tenantId);

		permission.save();
		return permission;
	}
}
