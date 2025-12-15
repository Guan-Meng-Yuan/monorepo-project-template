<script setup lang="tsx">
import { computed, ref, watch } from 'vue';
import type { SelectOption } from 'naive-ui';
import { enableStatusOptions, menuIconTypeOptions, menuTypeOptions } from '@/constants/business';
import type { MenuModel } from '@/service/api';
import { fetchGetAllRoles, fetchSaveOrUpdateMenu } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { getLocalIcons } from '@/utils/icon';
import { $t } from '@/locales';
import SvgIcon from '@/components/custom/svg-icon.vue';
import {
  getLayoutAndPage,
  getPathParamFromRoutePath,
  getRoutePathByRouteName,
  getRoutePathWithParam,
  transformLayoutAndPageToComponent
} from './shared';

defineOptions({
  name: 'MenuOperateModal'
});

export type OperateType = NaiveUI.TableOperateType | 'addChild' | 'addButton';

interface Props {
  /** the type of operation */
  operateType: OperateType;
  /** the edit menu data or the parent menu data when adding a child menu */
  rowData?: Api.SystemManage.Menu | null;
  /** all pages */
  allPages: string[];
}

const props = defineProps<Props>();

interface Emits {
  (e: 'submitted'): void;
}

const emit = defineEmits<Emits>();

const visible = defineModel<boolean>('visible', {
  default: false
});

const { formRef, validate, restoreValidation } = useNaiveForm();
const { defaultRequiredRule } = useFormRules();
const model = ref(createDefaultModel());
const title = computed(() => {
  const { menuType } = model.value;
  const { operateType } = props;

  // 编辑操作
  if (operateType === 'edit') {
    // 如果是按钮类型，显示"编辑权限按钮"
    if (menuType === 3) {
      return $t('page.manage.menu.editButton');
    }
    // 如果是目录或菜单类型，显示"编辑菜单"
    return $t('page.manage.menu.editMenu');
  }

  // 新增操作
  // 如果是按钮类型（menuType === 3）
  if (menuType === 3) {
    return $t('page.manage.menu.addButton');
  }

  // 如果是目录或菜单类型（menuType === 1 或 2）
  if (operateType === 'addChild') {
    return $t('page.manage.menu.addChildMenu');
  }
  // add
  return $t('page.manage.menu.addMenu');
});

function createDefaultModel(): MenuModel {
  return {
    menuType: 1,
    title: '',
    name: '',
    path: '',
    pathParam: '',
    component: '',
    layout: 'base',
    page: '',
    i18nKey: null,
    icon: '',
    iconType: 1,
    parentId: null,
    status: 1,
    keepAlive: false,
    constant: false,
    order: 0,
    href: null,
    hideInMenu: false,
    activeMenu: null,
    multiTab: false,
    fixedIndexInTab: null,
    query: []
  };
}

type RuleKey = Extract<keyof MenuModel, 'title' | 'status' | 'name' | 'path'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  title: defaultRequiredRule,
  status: defaultRequiredRule,
  name: defaultRequiredRule,
  path: defaultRequiredRule
};

const disabledMenuType = computed(() => props.operateType === 'edit');

const localIcons = getLocalIcons();
const localIconOptions = localIcons.map<SelectOption>(item => ({
  label: () => (
    <div class="flex-y-center gap-16px">
      <SvgIcon localIcon={item} class="text-icon" />
      <span>{item}</span>
    </div>
  ),
  value: item
}));

const showLayout = computed(
  () => (model.value.parentId === null || model.value.parentId === undefined) && model.value.menuType !== 3
);

const showPage = computed(() => model.value.menuType === 2);

const pageOptions = computed(() => {
  const allPages = [...props.allPages];

  if (model.value.name && !allPages.includes(model.value.name)) {
    allPages.unshift(model.value.name);
  }

  const opts: CommonType.Option[] = allPages.map(page => ({
    label: page,
    value: page
  }));

  return opts;
});

const layoutOptions: CommonType.Option[] = [
  {
    label: 'base',
    value: 'base'
  },
  {
    label: 'blank',
    value: 'blank'
  }
];

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    const options = data.map(item => ({
      label: item.roleName,
      value: item.roleCode
    }));

    roleOptions.value = [...options];
  }
}

function handleInitModel() {
  model.value = createDefaultModel();

  if (!props.rowData) return;

  if (props.operateType === 'addChild') {
    const { id } = props.rowData;

    Object.assign(model.value, { parentId: id });
  }

  if (props.operateType === 'addButton') {
    const { id } = props.rowData;

    Object.assign(model.value, { parentId: id, menuType: 3 });
  }

  if (props.operateType === 'edit') {
    const { component, ...rest } = props.rowData;

    const { layout, page } = getLayoutAndPage(component);
    const { path, param } = getPathParamFromRoutePath(rest.path);

    Object.assign(model.value, rest, { layout, page, routePath: path, pathParam: param });
  }

  if (!model.value.query) {
    model.value.query = [];
  }
}

function closeDrawer() {
  visible.value = false;
}

function handleUpdateRoutePathByRouteName() {
  if (model.value.name && model.value.menuType !== 3) {
    model.value.path = getRoutePathByRouteName(model.value.name);
  } else {
    model.value.path = '';
  }
}

// function handleUpdateI18nKeyByRouteName() {
//   if (model.value.name) {
//     model.value.i18nKey = `route.${model.value.name}` as App.I18n.I18nKey;
//   } else {
//     model.value.i18nKey = null;
//   }
// }

function getSubmitParams() {
  const { layout, page, pathParam, ...params } = model.value;

  const component = transformLayoutAndPageToComponent(layout, page);
  const routePath = getRoutePathWithParam(model.value.path, pathParam);

  params.component = component;
  params.path = routePath;

  return params;
}

async function handleSubmit() {
  await validate();

  const params = getSubmitParams();

  const { data } = await fetchSaveOrUpdateMenu(params);
  if (data) {
    window.$message?.success($t('common.updateSuccess'));
    closeDrawer();
    emit('submitted');
  }
}

watch(visible, () => {
  if (visible.value) {
    handleInitModel();
    restoreValidation();
    getRoleOptions();
  }
});

watch(
  () => model.value.name,
  () => {
    handleUpdateRoutePathByRouteName();
    // handleUpdateI18nKeyByRouteName();
  }
);
</script>

<template>
  <NModal v-model:show="visible" :title="title" preset="card" class="w-800px">
    <NScrollbar class="h-480px pr-20px">
      <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="100">
        <NGrid responsive="screen" item-responsive>
          <NFormItemGi span="24 m:12" :label="$t('page.manage.menu.menuType')" path="menuType">
            <NRadioGroup v-model:value="model.menuType" :disabled="disabledMenuType">
              <NRadio v-for="item in menuTypeOptions" :key="item.value" :value="item.value" :label="$t(item.label)" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi
            span="24 m:12"
            :label="model.menuType === 3 ? '按钮名称' : $t('page.manage.menu.menuName')"
            path="title"
          >
            <NInput
              v-model:value="model.title"
              :placeholder="model.menuType === 3 ? '请输入按钮名称' : $t('page.manage.menu.form.menuName')"
            />
          </NFormItemGi>
          <NFormItemGi
            span="24 m:12"
            :label="model.menuType === 3 ? '按钮编码' : $t('page.manage.menu.routeName')"
            path="name"
          >
            <NInput
              v-model:value="model.name"
              :placeholder="model.menuType === 3 ? '请输入按钮编码' : $t('page.manage.menu.form.routeName')"
            />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.routePath')"
            path="routePath"
          >
            <NInput v-model:value="model.path" disabled :placeholder="$t('page.manage.menu.form.routePath')" />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.pathParam')"
            path="pathParam"
          >
            <NInput v-model:value="model.pathParam" :placeholder="$t('page.manage.menu.form.pathParam')" />
          </NFormItemGi>
          <NFormItemGi v-if="showLayout" span="24 m:12" :label="$t('page.manage.menu.layout')" path="layout">
            <NSelect
              v-model:value="model.layout"
              :options="layoutOptions"
              :placeholder="$t('page.manage.menu.form.layout')"
            />
          </NFormItemGi>
          <NFormItemGi v-if="showPage" span="24 m:12" :label="$t('page.manage.menu.page')" path="page">
            <NSelect
              v-model:value="model.page"
              :options="pageOptions"
              :placeholder="$t('page.manage.menu.form.page')"
            />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.i18nKey')"
            path="i18nKey"
          >
            <NInput v-model:value="model.i18nKey" :placeholder="$t('page.manage.menu.form.i18nKey')" />
          </NFormItemGi>
          <NFormItemGi v-if="model.menuType !== 3" span="24 m:12" :label="$t('page.manage.menu.order')" path="order">
            <NInputNumber v-model:value="model.order" class="w-full" :placeholder="$t('page.manage.menu.form.order')" />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.iconTypeTitle')"
            path="iconType"
          >
            <NRadioGroup v-model:value="model.iconType">
              <NRadio
                v-for="item in menuIconTypeOptions"
                :key="item.value"
                :value="item.value"
                :label="$t(item.label)"
              />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="model.menuType !== 3" span="24 m:12" :label="$t('page.manage.menu.icon')" path="icon">
            <template v-if="model.iconType === 1">
              <NInput v-model:value="model.icon" :placeholder="$t('page.manage.menu.form.icon')" class="flex-1">
                <template #suffix>
                  <SvgIcon v-if="model.icon" :icon="model.icon" class="text-icon" />
                </template>
              </NInput>
            </template>
            <template v-if="model.iconType === 2">
              <NSelect
                v-model:value="model.icon"
                :placeholder="$t('page.manage.menu.form.localIcon')"
                :options="localIconOptions"
              />
            </template>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.menuStatus')"
            path="status"
          >
            <NRadioGroup v-model:value="model.status">
              <NRadio
                v-for="item in enableStatusOptions"
                :key="item.value"
                :value="item.value"
                :label="$t(item.label)"
              />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.keepAlive')"
            path="keepAlive"
          >
            <NRadioGroup v-model:value="model.keepAlive">
              <NRadio :value="true" :label="$t('common.yesOrNo.yes')" />
              <NRadio :value="false" :label="$t('common.yesOrNo.no')" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.constant')"
            path="constant"
          >
            <NRadioGroup v-model:value="model.constant">
              <NRadio :value="true" :label="$t('common.yesOrNo.yes')" />
              <NRadio :value="false" :label="$t('common.yesOrNo.no')" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi v-if="model.menuType !== 3" span="24 m:12" :label="$t('page.manage.menu.href')" path="href">
            <NInput v-model:value="model.href" :placeholder="$t('page.manage.menu.form.href')" />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.hideInMenu')"
            path="hideInMenu"
          >
            <NRadioGroup v-model:value="model.hideInMenu">
              <NRadio :value="true" :label="$t('common.yesOrNo.yes')" />
              <NRadio :value="false" :label="$t('common.yesOrNo.no')" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3 && model.hideInMenu"
            span="24 m:12"
            :label="$t('page.manage.menu.activeMenu')"
            path="activeMenu"
          >
            <NSelect
              v-model:value="model.activeMenu"
              :options="pageOptions"
              clearable
              :placeholder="$t('page.manage.menu.form.activeMenu')"
            />
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.multiTab')"
            path="multiTab"
          >
            <NRadioGroup v-model:value="model.multiTab">
              <NRadio :value="true" :label="$t('common.yesOrNo.yes')" />
              <NRadio :value="false" :label="$t('common.yesOrNo.no')" />
            </NRadioGroup>
          </NFormItemGi>
          <NFormItemGi
            v-if="model.menuType !== 3"
            span="24 m:12"
            :label="$t('page.manage.menu.fixedIndexInTab')"
            path="fixedIndexInTab"
          >
            <NInputNumber
              v-model:value="model.fixedIndexInTab"
              class="w-full"
              clearable
              :placeholder="$t('page.manage.menu.form.fixedIndexInTab')"
            />
          </NFormItemGi>
          <NFormItemGi v-if="model.menuType !== 3" span="24" :label="$t('page.manage.menu.query')" path="query">
            <NDynamicInput
              v-model:value="model.query"
              preset="pair"
              :key-placeholder="$t('page.manage.menu.form.queryKey')"
              :value-placeholder="$t('page.manage.menu.form.queryValue')"
            >
              <template #action="{ index, create, remove }">
                <NSpace class="ml-12px">
                  <NButton size="medium" @click="() => create(index)">
                    <icon-ic:round-plus class="text-icon" />
                  </NButton>
                  <NButton size="medium" @click="() => remove(index)">
                    <icon-ic-round-remove class="text-icon" />
                  </NButton>
                </NSpace>
              </template>
            </NDynamicInput>
          </NFormItemGi>
        </NGrid>
      </NForm>
    </NScrollbar>
    <template #footer>
      <NSpace justify="end" :size="16">
        <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
        <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
      </NSpace>
    </template>
  </NModal>
</template>

<style scoped></style>
