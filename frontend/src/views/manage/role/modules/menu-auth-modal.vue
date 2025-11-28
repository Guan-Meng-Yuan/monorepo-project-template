<script setup lang="ts">
import { computed, h, shallowRef, watch } from 'vue';
import type { TreeOption } from 'naive-ui';
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

const title = computed(() => $t('common.edit') + $t('page.manage.role.menuAuth'));

const home = shallowRef('');

async function getHome() {
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
  const { error, data } = await fetchSaveRolePermission({
    roleId: props.roleId,
    permissionIds: checks.value
  });
  if (!error && data) {
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

function renderLabel({ option }: { option: TreeOption }) {
  if (option.i18nKey) {
    const label = $t(option.i18nKey as App.I18n.I18nKey);
    return h('div', label);
  }
  return h('div', (option as Api.SystemManage.Menu).menuName);
}
</script>

<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-480px">
    <!--
 <div class="flex-y-center gap-16px pb-12px">
      <div>{{ $t('page.manage.menu.home') }}</div>
      <NSelect :value="home" :options="pageSelectOptions" size="small" class="w-160px" @update:value="updateHome" />
    </div>
-->
    <NTree
      v-model:checked-keys="checks"
      :data="tree"
      key-field="id"
      checkable
      expand-on-click
      :render-label="renderLabel"
      virtual-scroll
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
