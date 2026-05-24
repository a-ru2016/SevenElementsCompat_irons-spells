package io.github.xrickastley.sevenelementscompat.ironsspells;

import io.github.xrickastley.sevenelements.component.ElementalInfusionComponent;
import io.github.xrickastley.sevenelements.element.Element;
import io.github.xrickastley.sevenelements.element.ElementalApplication;
import io.github.xrickastley.sevenelements.element.ElementalApplications;
import io.github.xrickastley.sevenelements.element.InternalCooldownContext;
import io.github.xrickastley.sevenelements.element.InternalCooldownTag;
import io.github.xrickastley.sevenelements.element.InternalCooldownType;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(SevenElementsCompat.MODID)
public class SevenElementsCompat {
    public static final String MODID = "sevenelementscompat_irons_spells";
    private static final Logger LOGGER = LogManager.getLogger();

    private static final Map<String, Element> SIMPLY_SWORDS_MAP = new HashMap<>();
    private static final Map<ResourceLocation, Element> IRONS_SPELLS_MAP = new HashMap<>();
    private static final Map<String, Element> DAMAGE_TYPE_MAP = new HashMap<>();
    
    private static DataComponentType<ElementalInfusionComponent> elementalInfusionComponent = null;

    static {
        // Simply Swords mappings
        SIMPLY_SWORDS_MAP.put("brimstone_claymore", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("emberblade", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("hearthflame", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("soulpyre", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("molten_edge", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("sunfire", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("emberlash", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("flamewind", Element.PYRO);
        SIMPLY_SWORDS_MAP.put("wickpiercer", Element.PYRO);
        
        SIMPLY_SWORDS_MAP.put("livyatan", Element.HYDRO);
        SIMPLY_SWORDS_MAP.put("chompolotl", Element.HYDRO);
        
        SIMPLY_SWORDS_MAP.put("storms_edge", Element.ELECTRO);
        SIMPLY_SWORDS_MAP.put("stormbringer", Element.ELECTRO);
        SIMPLY_SWORDS_MAP.put("thunderbrand", Element.ELECTRO);
        SIMPLY_SWORDS_MAP.put("mjolnir", Element.ELECTRO);
        
        SIMPLY_SWORDS_MAP.put("bramblethorn", Element.DENDRO);
        SIMPLY_SWORDS_MAP.put("toxic_longsword", Element.DENDRO);
        SIMPLY_SWORDS_MAP.put("hiveheart", Element.DENDRO);
        
        SIMPLY_SWORDS_MAP.put("frostfall", Element.CRYO);
        SIMPLY_SWORDS_MAP.put("icewhisper", Element.CRYO);
        
        SIMPLY_SWORDS_MAP.put("whisperwind", Element.ANEMO);
        SIMPLY_SWORDS_MAP.put("tempest", Element.ANEMO);

        // Iron's Spells mappings
        IRONS_SPELLS_MAP.put(SchoolRegistry.FIRE_RESOURCE, Element.PYRO);
        IRONS_SPELLS_MAP.put(SchoolRegistry.ICE_RESOURCE, Element.CRYO);
        IRONS_SPELLS_MAP.put(SchoolRegistry.LIGHTNING_RESOURCE, Element.ELECTRO);
        IRONS_SPELLS_MAP.put(SchoolRegistry.NATURE_RESOURCE, Element.DENDRO);
        IRONS_SPELLS_MAP.put(ResourceLocation.fromNamespaceAndPath("aces_spell_utils", "hydro"), Element.HYDRO);
        IRONS_SPELLS_MAP.put(ResourceLocation.fromNamespaceAndPath("hazennstuff", "hydro"), Element.HYDRO);
        IRONS_SPELLS_MAP.put(ResourceLocation.fromNamespaceAndPath("hazen_n_stuff", "hydro"), Element.HYDRO);

        // Damage Type mappings
        DAMAGE_TYPE_MAP.put("irons_spellbooks:fire_magic", Element.PYRO);
        DAMAGE_TYPE_MAP.put("irons_spellbooks:ice_magic", Element.CRYO);
        DAMAGE_TYPE_MAP.put("irons_spellbooks:lightning_magic", Element.ELECTRO);
        DAMAGE_TYPE_MAP.put("irons_spellbooks:nature_magic", Element.DENDRO);
        DAMAGE_TYPE_MAP.put("aces_spell_utils:hydro_magic", Element.HYDRO);
        DAMAGE_TYPE_MAP.put("aces_spell_utils:hydro", Element.HYDRO);
        DAMAGE_TYPE_MAP.put("hazennstuff:hydro_magic", Element.HYDRO);
        DAMAGE_TYPE_MAP.put("hazennstuff:hydro", Element.HYDRO);
        DAMAGE_TYPE_MAP.put("hazen_n_stuff:hydro_magic", Element.HYDRO);
        DAMAGE_TYPE_MAP.put("hazen_n_stuff:hydro", Element.HYDRO);
    }

    public SevenElementsCompat() {
        LOGGER.info("SevenElementsCompat_irons-spells initialized.");
    }

    @SuppressWarnings("unchecked")
    public static DataComponentType<ElementalInfusionComponent> getElementalInfusionComponent() {
        if (elementalInfusionComponent == null) {
            elementalInfusionComponent = (DataComponentType<ElementalInfusionComponent>) BuiltInRegistries.DATA_COMPONENT_TYPE.get(
                ResourceLocation.fromNamespaceAndPath("seven-elements", "elemental_infusion")
            );
        }
        return elementalInfusionComponent;
    }

    public static boolean hasDynamicInfusion(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if ("simplyswords".equals(id.getNamespace())) {
            return SIMPLY_SWORDS_MAP.containsKey(id.getPath());
        }

        try {
            if (ISpellContainer.isSpellContainer(stack)) {
                var container = ISpellContainer.get(stack);
                if (container != null) {
                    for (var slot : container.getActiveSpells()) {
                        SchoolType school = slot.getSpell().getSchoolType();
                        if (school != null && IRONS_SPELLS_MAP.containsKey(school.getId())) {
                            return true;
                        }
                    }
                }
            }
        } catch (NoClassDefFoundError | Exception e) {
            // Fallback if Iron's Spells is not loaded
        }

        return false;
    }

    public static ElementalInfusionComponent getDynamicInfusion(ItemStack stack) {
        if (stack.isEmpty()) return null;

        Element element = null;
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if ("simplyswords".equals(id.getNamespace())) {
            element = SIMPLY_SWORDS_MAP.get(id.getPath());
        } else {
            try {
                if (ISpellContainer.isSpellContainer(stack)) {
                    var container = ISpellContainer.get(stack);
                    if (container != null) {
                        for (var slot : container.getActiveSpells()) {
                            SchoolType school = slot.getSpell().getSchoolType();
                            if (school != null) {
                                element = IRONS_SPELLS_MAP.get(school.getId());
                                if (element != null) {
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (NoClassDefFoundError | Exception e) {
                // Ignore
            }
        }

        if (element != null) {
            ElementalApplication.Builder infusionBuilder = ElementalApplications.builder()
                .setType(ElementalApplication.Type.GAUGE_UNIT)
                .setElement(element)
                .setGaugeUnits(1.0)
                .setAsAura(false);

            InternalCooldownContext.Builder icdBuilder = InternalCooldownContext.builder()
                .setTag(InternalCooldownTag.NONE)
                .setType(InternalCooldownType.DEFAULT);

            return new ElementalInfusionComponent(infusionBuilder, icdBuilder);
        }

        return null;
    }

    public static ElementalInfusionComponent getRealInfusion(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        var componentType = getElementalInfusionComponent();
        if (componentType == null) return null;
        
        ActiveElementHolder.BYPASS_DYNAMIC_INFUSION.set(true);
        try {
            return stack.get(componentType);
        } finally {
            ActiveElementHolder.BYPASS_DYNAMIC_INFUSION.set(false);
        }
    }

    public static Object getInfusedElementOfItem(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        try {
            var infusion = getRealInfusion(stack);
            if (infusion != null) {
                return infusion.getElement();
            }
        } catch (Throwable e) {
            // Ignore
        }
        return null;
    }

    public static void setSlotInfusedElement(ItemStack bookStack, int slotIndex, ElementalInfusionComponent component) {
        if (bookStack == null || bookStack.isEmpty()) return;

        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, bookStack, tag -> {
            net.minecraft.nbt.CompoundTag spellsTag;
            if (tag.contains("SevenElementsCompatSpells", 10)) {
                spellsTag = tag.getCompound("SevenElementsCompatSpells");
            } else {
                spellsTag = new net.minecraft.nbt.CompoundTag();
                tag.put("SevenElementsCompatSpells", spellsTag);
            }

            if (component == null) {
                spellsTag.remove(String.valueOf(slotIndex));
            } else {
                try {
                    net.minecraft.nbt.Tag compTag = ElementalInfusionComponent.CODEC.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, component)
                        .result().orElse(null);
                    if (compTag != null) {
                        spellsTag.put(String.valueOf(slotIndex), compTag);
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to encode ElementalInfusionComponent for slot " + slotIndex, e);
                }
            }

            if (spellsTag.isEmpty()) {
                tag.remove("SevenElementsCompatSpells");
            }
        });
    }

    public static ElementalInfusionComponent getSlotInfusedElement(ItemStack bookStack, int slotIndex) {
        if (bookStack == null || bookStack.isEmpty()) return null;
        
        net.minecraft.world.item.component.CustomData customData = bookStack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
        if (customData == null) return null;
        
        net.minecraft.nbt.CompoundTag tag = customData.copyTag();
        if (tag.contains("SevenElementsCompatSpells", 10)) {
            net.minecraft.nbt.CompoundTag spellsTag = tag.getCompound("SevenElementsCompatSpells");
            String key = String.valueOf(slotIndex);
            if (spellsTag.contains(key)) {
                try {
                    net.minecraft.nbt.Tag compTag = spellsTag.get(key);
                    return ElementalInfusionComponent.CODEC.parse(net.minecraft.nbt.NbtOps.INSTANCE, compTag)
                        .result().orElse(null);
                } catch (Exception e) {
                    LOGGER.error("Failed to decode ElementalInfusionComponent for slot " + slotIndex, e);
                }
            }
        }
        return null;
    }

    public static void cleanupSlotInfusedElements(ItemStack bookStack) {
        if (bookStack == null || bookStack.isEmpty()) return;
        
        try {
            if (ISpellContainer.isSpellContainer(bookStack)) {
                var container = ISpellContainer.get(bookStack);
                if (container != null) {
                    java.util.Set<Integer> validIndices = new java.util.HashSet<>();
                    for (var slot : container.getActiveSpells()) {
                        validIndices.add(slot.index());
                    }
                    
                    net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA, bookStack, tag -> {
                        if (tag.contains("SevenElementsCompatSpells", 10)) {
                            net.minecraft.nbt.CompoundTag spellsTag = tag.getCompound("SevenElementsCompatSpells");
                            java.util.List<String> keysToRemove = new java.util.ArrayList<>();
                            for (String key : spellsTag.getAllKeys()) {
                                try {
                                    int idx = Integer.parseInt(key);
                                    if (!validIndices.contains(idx)) {
                                        keysToRemove.add(key);
                                    }
                                } catch (NumberFormatException e) {
                                    keysToRemove.add(key);
                                }
                            }
                            for (String key : keysToRemove) {
                                spellsTag.remove(key);
                            }
                            if (spellsTag.isEmpty()) {
                                tag.remove("SevenElementsCompatSpells");
                            }
                        }
                    });
                }
            }
        } catch (Throwable e) {
            // Ignore
        }
    }

    public static net.minecraft.world.damagesource.DamageSource wrapSpellDamage(net.minecraft.world.damagesource.DamageSource source, net.minecraft.world.entity.LivingEntity target) {
        try {
            if (source == null || target == null) return source;
            
            LOGGER.info("wrapSpellDamage called. Source type: {}", source.getClass().getName());
            
            net.minecraft.world.damagesource.DamageSource checkSource = source;
            if (source.getClass().getName().equals("io.github.xrickastley.sevenelements.element.ElementalDamageSource")) {
                LOGGER.info("Source is ElementalDamageSource, extracting original source...");
                try {
                    java.lang.reflect.Method getOriginalSourceMethod = source.getClass().getMethod("getOriginalSource");
                    Object original = getOriginalSourceMethod.invoke(source);
                    if (original instanceof net.minecraft.world.damagesource.DamageSource ds) {
                        checkSource = ds;
                        LOGGER.info("Extracted original source class: {}", ds.getClass().getName());
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to extract original source from ElementalDamageSource", e);
                }
            }

            // Custom Element Infusion Check (spellbook slot infusion or active cast context)
            Object customElementObj = null;
            net.minecraft.world.entity.Entity directEntityForInfusion = checkSource.getDirectEntity();
            if (directEntityForInfusion instanceof InfusedEntity ie) {
                String elementName = ie.sevenelementscompat$getInfusedElement();
                if (elementName != null && !elementName.isEmpty()) {
                    customElementObj = elementName;
                    LOGGER.info("Found custom infused element from direct entity: {}", customElementObj);
                }
            }
            
            if (customElementObj == null) {
                customElementObj = ActiveElementHolder.ACTIVE_CAST_ELEMENT.get();
                if (customElementObj != null) {
                    LOGGER.info("Found custom infused element from active cast context: {}", customElementObj);
                }
            }
            
            if (customElementObj != null) {
                Object result = createElementalDamageSource(checkSource, target, customElementObj);
                if (result instanceof net.minecraft.world.damagesource.DamageSource ds && ds != checkSource) {
                    return ds;
                }
                return source;
            }

            // 1. DamageType ID による判定 (堅牢でシリアライズ対応)
            net.minecraft.resources.ResourceLocation typeLoc = null;
            try {
                var holder = checkSource.typeHolder();
                if (holder != null) {
                    var optKey = holder.unwrapKey();
                    if (optKey.isPresent()) {
                        typeLoc = optKey.get().location();
                        LOGGER.info("DamageSource DamageType Location: {}", typeLoc);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to get damage type location", e);
            }

            Element matchedElement = null;
            if (typeLoc != null) {
                String typeStr = typeLoc.toString();
                if (DAMAGE_TYPE_MAP.containsKey(typeStr)) {
                    matchedElement = DAMAGE_TYPE_MAP.get(typeStr);
                    LOGGER.info("DamageType matched spell element from map: {} -> {}", typeStr, matchedElement);
                } else {
                    // Fallback keyword detection for damage type
                    matchedElement = getElementFromId(typeStr);
                    if (matchedElement != null) {
                        LOGGER.info("DamageType {} matched spell element via keyword fallback: {}", typeStr, matchedElement);
                    } else {
                        LOGGER.info("DamageType {} not recognized.", typeStr);
                    }
                }
            }

            if (matchedElement == null) {
                // 1.5. 直接攻撃エンティティ (directEntity) のクラス名による判定 (フォールバック)
                net.minecraft.world.entity.Entity directEntity = checkSource.getDirectEntity();
                if (directEntity != null) {
                    String className = directEntity.getClass().getName();
                    LOGGER.info("Direct damage entity class: {}", className);
                    matchedElement = getElementFromProjectileClass(className);
                    if (matchedElement != null) {
                        LOGGER.info("Matched element via projectile class: {}", matchedElement);
                    }
                }
            }

            if (matchedElement == null) {
                // 2. インスタンス判定 (フォールバック)
                try {
                    if (checkSource instanceof io.redspace.ironsspellbooks.damage.SpellDamageSource spellSource) {
                        LOGGER.info("Source is SpellDamageSource!");
                        var spell = spellSource.spell();
                        if (spell != null) {
                            var school = spell.getSchoolType();
                            if (school != null) {
                                ResourceLocation schoolId = school.getId();
                                LOGGER.info("Spell school: {}", schoolId);
                                matchedElement = IRONS_SPELLS_MAP.get(schoolId);
                                if (matchedElement == null) {
                                    // Fallback keyword detection for school
                                    matchedElement = getElementFromId(schoolId.toString());
                                }
                                if (matchedElement != null) {
                                    LOGGER.info("Found spell element via school (mapped or fallback): {}", matchedElement);
                                } else {
                                    LOGGER.info("School {} not recognized.", schoolId);
                                }
                            }
                        }
                    } else {
                        LOGGER.info("Source is NOT SpellDamageSource (Class: {}).", checkSource.getClass().getName());
                    }
                } catch (NoClassDefFoundError e) {
                    LOGGER.info("Iron's Spells classes not found during SpellDamageSource check.");
                }
            }

            if (matchedElement != null) {
                Object result = createElementalDamageSource(checkSource, target, matchedElement);
                if (result instanceof net.minecraft.world.damagesource.DamageSource ds && ds != checkSource) {
                    return ds;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error in wrapSpellDamage", e);
        }
        return source;
    }

    private static Element getElementFromId(String id) {
        String lowerId = id.toLowerCase();
        if (lowerId.contains("fire") || lowerId.contains("pyro") || lowerId.contains("burn")) {
            return Element.PYRO;
        }
        if (lowerId.contains("water") || lowerId.contains("hydro") || lowerId.contains("bubble") || lowerId.contains("rain")) {
            return Element.HYDRO;
        }
        if (lowerId.contains("ice") || lowerId.contains("cryo") || lowerId.contains("frost") || lowerId.contains("freeze")) {
            return Element.CRYO;
        }
        if (lowerId.contains("lightning") || lowerId.contains("electro") || lowerId.contains("thunder")) {
            return Element.ELECTRO;
        }
        if (lowerId.contains("nature") || lowerId.contains("dendro") || lowerId.contains("poison") || lowerId.contains("acid")) {
            return Element.DENDRO;
        }
        if (lowerId.contains("anemo") || lowerId.contains("wind") || lowerId.contains("air")) {
            return Element.ANEMO;
        }
        if (lowerId.contains("geo") || lowerId.contains("rock") || lowerId.contains("earth")) {
            return Element.GEO;
        }
        return null;
    }

    private static Element getElementFromProjectileClass(String className) {
        return getElementFromId(className);
    }

    public static Object createElementalDamageSource(Object source, Object target, Object elementObj) {
        try {
            LOGGER.info("createElementalDamageSource starting with elementObj: {}", elementObj);
            if (elementObj == null) return source;

            Class<?> elementalApplicationsClass = Class.forName("io.github.xrickastley.sevenelements.element.ElementalApplications");
            Class<?> elementClass = Class.forName("io.github.xrickastley.sevenelements.element.Element");
            
            LOGGER.info("Loaded SevenElements classes.");
            
            java.lang.reflect.Method gaugeUnitsMethod = null;
            for (java.lang.reflect.Method m : elementalApplicationsClass.getMethods()) {
                if (m.getName().equals("gaugeUnits") && m.getParameterCount() == 3) {
                    if (m.getParameterTypes()[1].getName().equals(elementClass.getName())) {
                        gaugeUnitsMethod = m;
                        break;
                    }
                }
            }
            
            if (gaugeUnitsMethod == null) {
                LOGGER.warn("gaugeUnitsMethod is null!");
                return source;
            }
            
            Object firstElement = null;
            java.util.List<Object> extraElements = new java.util.ArrayList<>();

            if (elementObj instanceof String s) {
                String[] parts = s.split(",");
                if (parts.length > 0) {
                    firstElement = Enum.valueOf((Class<Enum>) elementClass, parts[0].trim());
                    for (int i = 1; i < parts.length; i++) {
                        try {
                            extraElements.add(Enum.valueOf((Class<Enum>) elementClass, parts[i].trim()));
                        } catch (Exception ex) {
                            LOGGER.error("Failed to parse extra element: " + parts[i], ex);
                        }
                    }
                }
            } else if (elementObj.getClass().getName().equals(elementClass.getName())) {
                firstElement = elementObj;
            } else {
                // Cross-classloader safe resolution: resolve by name from the reflected class
                String name = elementObj.toString();
                firstElement = Enum.valueOf((Class<Enum>) elementClass, name);
            }

            if (firstElement == null) {
                return source;
            }
            
            LOGGER.info("First element: {}, extra elements: {}", firstElement, extraElements);
            Object application = gaugeUnitsMethod.invoke(null, target, firstElement, 1.0);
            LOGGER.info("Created ElementalApplication: {}", application);

            Class<?> internalCooldownContextClass = Class.forName("io.github.xrickastley.sevenelements.element.InternalCooldownContext");
            Class<?> entityClass = Class.forName("net.minecraft.world.entity.Entity");
            
            java.lang.reflect.Method ofDefaultMethod = null;
            for (java.lang.reflect.Method m : internalCooldownContextClass.getMethods()) {
                if (m.getName().equals("ofDefault") && m.getParameterCount() == 2) {
                    Class<?>[] params = m.getParameterTypes();
                    if (entityClass.isAssignableFrom(params[0]) && params[1].getName().equals("java.lang.String")) {
                        ofDefaultMethod = m;
                        break;
                    }
                }
            }
            
            if (ofDefaultMethod == null) {
                LOGGER.warn("ofDefaultMethod is null!");
                return source;
            }
            
            LOGGER.info("Found ofDefaultMethod: {}", ofDefaultMethod);
            Object attacker = ((net.minecraft.world.damagesource.DamageSource)source).getEntity();
            Object icd = ofDefaultMethod.invoke(null, attacker != null ? attacker : target, "seven-elements:spell_infusion");
            LOGGER.info("Created ICD: {}", icd);

            Class<?> elementalDamageSourceClass = Class.forName("io.github.xrickastley.sevenelements.element.ElementalDamageSource");
            Class<?> damageSourceClass = Class.forName("net.minecraft.world.damagesource.DamageSource");
            Class<?> elementalApplicationClass = Class.forName("io.github.xrickastley.sevenelements.element.ElementalApplication");
            
            java.lang.reflect.Constructor<?> constructor = null;
            for (java.lang.reflect.Constructor<?> c : elementalDamageSourceClass.getConstructors()) {
                if (c.getParameterCount() == 3) {
                    Class<?>[] paramTypes = c.getParameterTypes();
                    if (paramTypes[0].isAssignableFrom(damageSourceClass) &&
                        paramTypes[1].isAssignableFrom(elementalApplicationClass) &&
                        paramTypes[2].isAssignableFrom(internalCooldownContextClass)) {
                        constructor = c;
                        break;
                    }
                }
            }
            
            if (constructor == null) {
                LOGGER.warn("ElementalDamageSource constructor not found!");
                return source;
            }
            
            LOGGER.info("Found ElementalDamageSource constructor: {}", constructor);
            Object result = constructor.newInstance(source, application, icd);
            LOGGER.info("Successfully created ElementalDamageSource result!");

            // Add extra elements if any
            for (Object extraElement : extraElements) {
                try {
                    Object extraApplication = gaugeUnitsMethod.invoke(null, target, extraElement, 1.0);
                    java.lang.reflect.Method addAdditionalMethod = result.getClass().getMethod("addAdditionalApplication", elementalApplicationClass);
                    addAdditionalMethod.invoke(result, extraApplication);
                    LOGGER.info("Successfully added additional application to damage source: {}", extraElement);
                } catch (Exception e) {
                    LOGGER.error("Failed to add additional application: " + extraElement, e);
                }
            }

            return result;
        } catch (Exception e) {
            LOGGER.error("Failed to wrap spell damage into ElementalDamageSource", e);
            return source;
        }
    }
}
