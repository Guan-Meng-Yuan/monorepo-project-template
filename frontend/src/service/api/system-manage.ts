import { request } from '../request';

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

/** get menu tree */
export function fetchGetMenuTree() {
  return request<Api.SystemManage.MenuTree[]>({
    url: '/systemManage/getMenuTree',
    method: 'get'
  });
}
export type MenuModel = Pick<
  Api.SystemManage.Menu,
  | 'menuType'
  | 'title'
  | 'name'
  | 'path'
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
> & {
  query: NonNullable<Api.SystemManage.Menu['query']>;
  layout: string;
  page: string;
  pathParam: string;
};

/** menu data for API request (without layout, page, pathParam) */
export type MenuRequestData = Omit<MenuModel, 'layout' | 'page' | 'pathParam'>;

/** save or update menu */
export function fetchSaveOrUpdateMenu(data: MenuRequestData) {
  return request<boolean>({
    url: '/systemManage/saveOrUpdateMenu',
    method: 'post',
    data
  });
}

/** delete menu */
export function fetchDeleteMenu(ids: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteMenu/${ids}`,
    method: 'delete'
  });
}

/** get role permission ids */
export function fetchGetRolePermissionIds(roleId: string) {
  return request<string[]>({
    url: `/systemManage/getRolePermissionIds/${roleId}`,
    method: 'get'
  });
}

/** save role permission */
export function fetchSaveRolePermission(roleId: string, permissionIds: string[]) {
  return request<boolean>({
    url: '/systemManage/saveRolePermission',
    method: 'post',
    data: { roleId, permissionIds }
  });
}
export type RoleModel = Pick<Api.SystemManage.Role, 'roleName' | 'roleCode' | 'roleDesc' | 'status'>;

/** save or update role */
export function fetchSaveOrUpdateRole(data: RoleModel) {
  return request<boolean>({
    url: '/systemManage/saveOrUpdateRole',
    method: 'post',
    data
  });
}

/** delete role */
export function fetchDeleteRole(ids: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteRole/${ids}`,
    method: 'delete'
  });
}
export type UserModel = Pick<
  Api.SystemManage.User,
  'username' | 'userGender' | 'nickName' | 'userPhone' | 'userEmail' | 'userRoles' | 'status'
>;

/** save or update user */
export function fetchSaveOrUpdateUser(data: UserModel) {
  return request<boolean>({
    url: '/systemManage/saveOrUpdateUser',
    method: 'post',
    data
  });
}

/** delete user */
export function fetchDeleteUser(ids: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteUser/${ids}`,
    method: 'delete'
  });
}

/** delete tenant */
export function fetchDeleteTenant(ids: string[]) {
  return request<boolean>({
    url: `/systemManage/deleteTenant/${ids}`,
    method: 'delete'
  });
}
export type TenantModel = Pick<Api.SystemManage.Tenant, 'name' | 'code' | 'status'>;
/** save or update tenant */
export function fetchSaveOrUpdateTenant(data: TenantModel) {
  return request<boolean>({
    url: '/systemManage/saveOrUpdateTenant',
    method: 'post',
    data
  });
}

/** get tenant list */
export function fetchGetTenantList(params?: Api.SystemManage.TenantSearchParams) {
  return request<Api.SystemManage.TenantList>({
    url: '/systemManage/getTenantList',
    method: 'get',
    params
  });
}
