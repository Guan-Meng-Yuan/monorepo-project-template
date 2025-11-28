<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { jsonClone } from '@sa/utils';
import { enableStatusOptions } from '@/constants/business';
import type { TenantModel } from '@/service/api';
import { fetchAddOrUpdateTenant, fetchGetAllRoles } from '@/service/api';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';

defineOptions({
  name: 'TenantOperateDrawer'
});

interface Props {
  /** the type of operation */
  operateType: NaiveUI.TableOperateType;
  /** the edit row data */
  rowData?: Api.SystemManage.Tenant | null;
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

const title = computed(() => {
  const titles: Record<NaiveUI.TableOperateType, string> = {
    add: '新增租户',
    edit: '编辑租户'
  };
  return titles[props.operateType];
});

const model = ref(createDefaultModel());

function createDefaultModel(): TenantModel {
  return {
    name: '',
    code: '',
    desc: '',
    status: null
  };
}

type RuleKey = Extract<keyof TenantModel, 'name' | 'code' | 'status'>;

const rules: Record<RuleKey, App.Global.FormRule> = {
  name: defaultRequiredRule,
  code: defaultRequiredRule,
  status: defaultRequiredRule
};

/** the enabled role options */
const roleOptions = ref<CommonType.Option<string>[]>([]);

async function getRoleOptions() {
  const { error, data } = await fetchGetAllRoles();

  if (!error) {
    const options = data.map(item => ({
      label: item.roleName,
      value: item.id
    }));

    roleOptions.value = [...options];
  }
}

function handleInitModel() {
  model.value = createDefaultModel();

  if (props.operateType === 'edit' && props.rowData) {
    Object.assign(model.value, jsonClone(props.rowData));
  }
}

function closeDrawer() {
  visible.value = false;
}

async function handleSubmit() {
  await validate();
  const { data } = await fetchAddOrUpdateTenant(model.value);
  if (data) {
    window.$message?.success('操作成功');
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
</script>

<template>
  <NDrawer v-model:show="visible" display-directive="show" :width="360">
    <NDrawerContent :title="title" :native-scrollbar="false" closable>
      <NForm ref="formRef" :model="model" :rules="rules">
        <NFormItem label="租户名称" path="name">
          <NInput v-model:value="model.name" placeholder="请输入租户名称" />
        </NFormItem>
        <NFormItem label="租户编码" path="code">
          <NInput v-model:value="model.code" placeholder="请输入租户编码" />
        </NFormItem>
        <NFormItem label="租户状态" path="status">
          <NSelect v-model:value="model.status" placeholder="请选择租户状态" :options="enableStatusOptions" />
        </NFormItem>
      </NForm>
      <template #footer>
        <NSpace :size="16">
          <NButton @click="closeDrawer">{{ $t('common.cancel') }}</NButton>
          <NButton type="primary" @click="handleSubmit">{{ $t('common.confirm') }}</NButton>
        </NSpace>
      </template>
    </NDrawerContent>
  </NDrawer>
</template>

<style scoped></style>
