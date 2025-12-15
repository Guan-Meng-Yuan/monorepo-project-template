<script setup lang="ts">
import type { VNodeChild } from 'vue';
import { computed, h, shallowRef, watch } from 'vue';
import { NTag, type TreeOption } from 'naive-ui';
import { menuTypeRecord } from '@/constants/business';
import { fetchGetAllPages, fetchGetMenuTree, fetchGetRolePermissionIds, fetchSaveRolePermission } from '@/service/api';
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

const title = computed(() => `${$t('common.edit')}权限`);

const home = shallowRef('');

async function getHome() {
  console.log(props.roleId);

  home.value = 'home';
}

const pages = shallowRef<string[]>([]);

async function getPages() {
  const { error, data } = await fetchGetAllPages();

  if (!error) {
    pages.value = data;
  }
}

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

async function handleSubmit() {
  const { data } = await fetchSaveRolePermission(props.roleId, checks.value);
  if (data) {
    window.$message?.success?.($t('common.modifySuccess'));
    closeModal();
  }
}

function init() {
  getHome();
  getPages();
  getTree();
  getChecks();
}

watch(visible, val => {
  if (val) {
    init();
  }
});
const renderLabel = ({ option }: { option: TreeOption; checked: boolean; selected: boolean }): VNodeChild => {
  return option.i18nKey ? $t(option.i18nKey as App.I18n.I18nKey) : (option.title as string);
};
const renderSuffix = ({ option }: { option: TreeOption; checked: boolean; selected: boolean }): VNodeChild => {
  return h(
    NTag,
    { type: 'info', size: 'small', bordered: false },
    { default: () => $t(menuTypeRecord[option.menuType as Api.SystemManage.MenuType]) }
  );
};
</script>

<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-480px">
    <NTree
      v-model:checked-keys="checks"
      :data="tree"
      key-field="id"
      checkable
      default-expand-all
      expand-on-click
      :render-label="renderLabel"
      virtual-scroll
      :render-suffix="renderSuffix"
      block-line
      class="h-280px"
    />
    <template #footer>
      <NSpace justify="end">
        <NButton size="small" class="mt-16px" @click="closeModal">
          {{ $t('common.cancel') }}
        </NButton>
        <NButton type="primary" size="small" class="mt-16px" @click="handleSubmit">
          {{ $t('common.confirm') }}
        </NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>
