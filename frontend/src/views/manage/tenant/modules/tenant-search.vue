<script setup lang="ts">
import { computed, toRaw } from 'vue';
import { jsonClone } from '@sa/utils';
import { enableStatusOptions } from '@/constants/business';
import { useFormRules, useNaiveForm } from '@/hooks/common/form';
import { translateOptions } from '@/utils/common';
import { $t } from '@/locales';

defineOptions({
  name: 'TenantSearch'
});

interface Emits {
  (e: 'search'): void;
}

const emit = defineEmits<Emits>();

const { formRef, validate, restoreValidation } = useNaiveForm();

const model = defineModel<Api.SystemManage.TenantSearchParams>('model', { required: true });

type RuleKey = Extract<keyof Api.SystemManage.TenantSearchParams, 'code' | 'name'>;

const rules = computed<Record<RuleKey, App.Global.FormRule>>(() => {
  const { defaultRequiredRule } = useFormRules(); // inside computed to make locale reactive

  return {
    code: defaultRequiredRule,
    name: defaultRequiredRule,
    status: defaultRequiredRule
  };
});

const defaultModel = jsonClone(toRaw(model.value));

function resetModel() {
  Object.assign(model.value, defaultModel);
}

async function reset() {
  await restoreValidation();
  resetModel();
}

async function search() {
  await validate();
  emit('search');
}
</script>

<template>
  <NCard :bordered="false" size="small" class="card-wrapper">
    <NCollapse>
      <NCollapseItem :title="$t('common.search')" name="tenant-search">
        <NForm ref="formRef" :model="model" :rules="rules" label-placement="left" :label-width="80">
          <NGrid responsive="screen" item-responsive>
            <NFormItemGi span="24 s:12 m:6" label="租户名称" path="name" class="pr-24px">
              <NInput v-model:value="model.name" placeholder="请输入租户名称" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="租户编码" path="code" class="pr-24px">
              <NInput v-model:value="model.code" placeholder="请输入租户编码" />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6" label="租户状态" path="status" class="pr-24px">
              <NSelect
                v-model:value="model.status"
                placeholder="请选择租户状态"
                :options="translateOptions(enableStatusOptions)"
                label-field="label"
                clearable
              />
            </NFormItemGi>
            <NFormItemGi span="24 s:12 m:6">
              <NSpace class="w-full" justify="end">
                <NButton @click="reset">
                  <template #icon>
                    <icon-ic-round-refresh class="text-icon" />
                  </template>
                  {{ $t('common.reset') }}
                </NButton>
                <NButton type="primary" ghost @click="search">
                  <template #icon>
                    <icon-ic-round-search class="text-icon" />
                  </template>
                  {{ $t('common.search') }}
                </NButton>
              </NSpace>
            </NFormItemGi>
          </NGrid>
        </NForm>
      </NCollapseItem>
    </NCollapse>
  </NCard>
</template>

<style scoped></style>
