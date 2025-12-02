import { request } from '../request';
export type UserModel = Pick<
  Api.SystemManage.User,
  'userName' | 'userGender' | 'nickName' | 'userPhone' | 'userEmail' | 'userRoles' | 'status'
>;
export type TenantModel = Pick<Api.SystemManage.Tenant, 'name' | 'code' | 'desc' | 'status'>;

export type MenuModel = Pick<
  Api.SystemManage.Menu,
  | 'menuType'
  | 'menuName'
  | 'routeName'
  | 'routePath'
  | 'component'
  | 'order'
  | 'i18nKey'
  | 'icon'
  | 'iconType'
  | 'status'
  | 'parentId'
  | 'keepAlive'
  | 'constant'
  | 'href'
  | 'hideInMenu'
  | 'activeMenu'
  | 'multiTab'
  | 'fixedIndexInTab'
  | 'code'
  | 'desc'
> & {
  query: NonNullable<Api.SystemManage.Menu['query']>;
  layout: string;
  page: string;
  pathParam: string;
};
/** get role list */
export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
  return request<Api.SystemManage.RoleList>({
    url: '/systemManage/getRoleList',
    method: 'get',
    params
  });
}

/**
 * get all roles
 *
 * these roles are all enabled
 */
export function fetchGetAllRoles() {
  return request<Api.SystemManage.AllRole[]>({
    url: '/systemManage/getAllRoles',
    method: 'get'
  });
}

/** get user list */
export function fetchGetUserList(params?: Api.SystemManage.UserSearchParams) {
  return request<Api.SystemManage.UserList>({
    url: '/systemManage/getUserList',
    method: 'get',
    params
  });
}
export function fetchGetTenantList(params?: Api.SystemManage.TenantSearchParams) {
  return request<Api.SystemManage.TenantList>({
    url: '/systemManage/getTenantList',
    method: 'get',
    params
  });
}

/** get menu list */
export function fetchGetMenuList() {
  return request<Api.SystemManage.MenuList>({
    url: '/systemManage/getMenuList/v2',
    method: 'get'
  });
}

/** get all pages */
export function fetchGetAllPages() {
  return request<string[]>({
    url: '/systemManage/getAllPages',
    method: 'get'
  });
}
export function fetchGetRolePermissionIds(roleId: string) {
  return request<string[]>({
    url: `/systemManage/getRolePermissionId/${roleId}`,
    method: 'get'
  });
}
export function fetchSaveRolePermission(data: { roleId: string; permissionIds: string[] }) {
  return request<boolean>({
    url: `/systemManage/saveRolePermission/${data.roleId}`,
    method: 'post',
    data: data.permissionIds
  });
}
/** get menu tree */
export function fetchGetMenuTree() {
  return request<Api.SystemManage.MenuTree[]>({
    url: '/systemManage/getMenuTree',
    method: 'get'
  });
}
export function fetchAddOrUpdateUser(data: UserModel) {
  return request<boolean>({
    url: '/systemManage/addOrUpdateUser',
    method: 'post',
    data
  });
}
export function fetchAddOrUpdateTenant(data: TenantModel) {
  return request<boolean>({
    url: '/systemManage/addOrUpdateTenant',
    method: 'post',
    data
  });
}
export function fetchDeleteUser(userIds: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteUser/${userIds}`,
    method: 'delete'
  });
}
export function fetchSaveOrUpdateMenu(data: MenuModel) {
  return request<boolean>({
    url: '/systemManage/saveOrUpdateMenu',
    method: 'post',
    data
  });
}
export function fetchGetAllButtons() {
  return request<{ id: string; desc: string; code: string }[]>({
    url: '/systemManage/getAllButtons',
    method: 'get'
  });
}
export function fetchGetRoleButtonPermissions(roleId: string) {
  return request<string[]>({
    url: `/systemManage/getRoleButtonPermissions/${roleId}`,
    method: 'get'
  });
}
export type RoleModel = Pick<Api.SystemManage.Role, 'roleName' | 'roleCode' | 'roleDesc' | 'status'>;
export function fetchAddOrUpdateRole(data: RoleModel) {
  return request<boolean>({
    url: '/systemManage/addOrUpdateRole',
    method: 'post',
    data
  });
}
export function fetchDeleteRole(roleIds: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteRole/${roleIds}`,
    method: 'delete'
  });
}
export function fetchDeleteMenu(menuIds: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteMenu/${menuIds}`,
    method: 'delete'
  });
}
export function fetchDeleteTenant(tenantIds: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteTenant/${tenantIds}`,
    method: 'delete'
  });
}
