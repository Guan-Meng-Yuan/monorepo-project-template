<script setup lang="ts">
import { computed, h, ref, shallowRef, watch } from 'vue';
import type { TreeOption } from 'naive-ui';
import { NTag } from 'naive-ui';
import { menuTypeRecord } from '@/constants/business';
import { fetchGetMenuTree, fetchGetRolePermissionIds, fetchSaveRolePermission } from '@/service/api';
import { $t } from '@/locales';
defineOptions({
  name: 'MenuAuthModal'
});

interface Props {
  /** the roleId */
  roleId: string;
}

const props = defineProps<Props>();

const visible = defineModel<boolean>('visible', {
  default: false
});

function closeModal() {
  visible.value = false;
}

const title = computed(() => `${$t('common.edit')}角色权限`);

const tree = shallowRef<Api.SystemManage.MenuTree[]>([]);

async function getTree() {
  const { error, data } = await fetchGetMenuTree();

  if (!error) {
    tree.value = data;
  }
}

const checks = shallowRef<string[]>([]);

async function getChecks() {
  const { error, data } = await fetchGetRolePermissionIds(props.roleId);

  if (!error) {
    checks.value = data;
  }
}
const loading = ref(false);
async function handleSubmit() {
  loading.value = true;
  const { error, data } = await fetchSaveRolePermission({
    roleId: props.roleId,
    permissionIds: checks.value
  });
  loading.value = false;
  if (!error && data) {
    window.$message?.success?.($t('common.modifySuccess'));
    closeModal();
  }
}

function init() {
  getTree();
  getChecks();
}

watch(visible, val => {
  if (val) {
    init();
  }
});

function renderLabel({ option }: { option: TreeOption }) {
  if (option.i18nKey) {
    const label = $t(option.i18nKey as App.I18n.I18nKey);
    return h('div', label);
  }
  return h('div', (option as Api.SystemManage.Menu).menuName);
}

function renderSuffix({ option }: { option: TreeOption }) {
  const tagMap: Record<Api.SystemManage.MenuType, NaiveUI.ThemeColor> = {
    1: 'default',
    2: 'primary',
    3: 'success'
  };

  const label = $t(menuTypeRecord[option.menuType as Api.SystemManage.MenuType]);

  return h(
    NTag,
    { type: tagMap[option.menuType as Api.SystemManage.MenuType], bordered: false, size: 'small' },
    { default: () => label }
  );
}
</script>

<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-480px">
    <NTree
      v-model:checked-keys="checks"
      :data="tree"
      key-field="id"
      checkable
      expand-on-click
      :render-label="renderLabel"
      :render-suffix="renderSuffix"
      virtual-scroll
      block-line
      class="h-280px"
    />
    <template #footer>
      <NSpace justify="end">
        <NButton size="small" class="mt-16px" @click="closeModal">
          {{ $t('common.cancel') }}
        </NButton>
        <NButton type="primary" :loading="loading" size="small" class="mt-16px" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>
