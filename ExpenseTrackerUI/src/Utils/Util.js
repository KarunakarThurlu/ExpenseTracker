export function getGraphQLEnumValue(enumType, label) {
  const map = enumValueMap[enumType];
  if (!map) throw new Error(`No enum map found for ${enumType}`);
  const value = map[label];
  if (!value) throw new Error(`No enum value found for label '${label}' in ${enumType}`);
  return value;
}

