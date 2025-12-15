import { $t } from '@/locales';

/**
 * Transform record to option
 *
 * @example
 *   ```ts
 *   // String keys
 *   const record = {
 *     key1: 'label1',
 *     key2: 'label2'
 *   };
 *   const options = transformRecordToOption(record);
 *   // [
 *   //   { value: 'key1', label: 'label1' },
 *   //   { value: 'key2', label: 'label2' }
 *   // ]
 *
 *   // Number keys
 *   const numRecord = {
 *     1: 'label1',
 *     2: 'label2'
 *   };
 *   const numOptions = transformRecordToOption(numRecord);
 *   // [
 *   //   { value: 1, label: 'label1' },
 *   //   { value: 2, label: 'label2' }
 *   // ]
 *   ```;
 *
 * @param record
 */
export function transformRecordToOption<T extends Record<string | number, string>>(
  record: T
): CommonType.Option<string | number, T[keyof T]>[] {
  return Object.entries(record).map(([value, label]) => {
    // Check if the key is a numeric string, convert to number if so
    const numValue = Number(value);
    const isNumeric = !Number.isNaN(numValue) && value === String(numValue) && value.trim() !== '';

    return {
      value: isNumeric ? numValue : value,
      label
    };
  }) as CommonType.Option<string | number, T[keyof T]>[];
}

/**
 * Translate options
 *
 * @param options
 */
export function translateOptions(options: CommonType.Option<string | number, App.I18n.I18nKey>[]) {
  return options.map(option => ({
    ...option,
    label: $t(option.label)
  }));
}

/**
 * Toggle html class
 *
 * @param className
 */
export function toggleHtmlClass(className: string) {
  function add() {
    document.documentElement.classList.add(className);
  }

  function remove() {
    document.documentElement.classList.remove(className);
  }

  return {
    add,
    remove
  };
}
