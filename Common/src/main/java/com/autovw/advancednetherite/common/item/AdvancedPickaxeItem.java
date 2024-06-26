package com.autovw.advancednetherite.common.item;

import com.autovw.advancednetherite.AdvancedNetherite;
import com.autovw.advancednetherite.api.annotation.Internal;
import com.autovw.advancednetherite.common.AdvancedUtil;
import com.autovw.advancednetherite.config.ConfigHelper;
import com.autovw.advancednetherite.core.util.ModTooltips;
import com.autovw.advancednetherite.core.util.ModToolTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;

/**
 * @author Autovw
 */
public class AdvancedPickaxeItem extends PickaxeItem
{
    private final Tier tier;

    public AdvancedPickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
        this.tier = tier;
    }

    @Internal
    public AdvancedPickaxeItem(Tier tier, int attackDamage, float attackSpeed)
    {
        this(tier, new Properties().attributes(createAttributes(tier, attackDamage, attackSpeed)).fireResistant());
    }

    /**
     * {@link Override} this method if you want to add your own custom tooltips.
     *
     * @param stack     The item stack
     * @param context   The tooltip context
     * @param tooltips  List of tooltips
     * @param flag      Used to determine if a tooltip is only visible when debug mode (F3 + H) is enabled
     */
    public void addTooltips(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag flag)
    {
    }

    /**
     * {@link Override} this method if you want to give your item a custom durability bar color.
     * Feature is disabled by default, can be enabled in Advanced Netherite's Client config.
     *
     * @param stack The item stack
     * @return The custom durability bar color
     */
    public ChatFormatting customDurabilityBarColor(ItemStack stack)
    {
        return null;
    }

    /* ================ INTERNAL, use alternatives linked in javadoc ================ */

    /**
     * Don't override this method, use {@link AdvancedPickaxeItem#addTooltips(ItemStack, TooltipContext, List, TooltipFlag)} if you want to add your own custom tooltips.
     */
    @Internal
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag)
    {
        if (ConfigHelper.get().getClient().showTooltips())
        {
            if (AdvancedNetherite.getRegistryHelper().getItemById(stack.getItem()).getNamespace().equals(AdvancedNetherite.MOD_ID) && ConfigHelper.get().getCommon().getAdditionalDrops().enableAdditionalOreDrops())
            {
                if (Screen.hasShiftDown())
                {
                    if (tier == ModToolTiers.NETHERITE_IRON) tooltip.add(ModTooltips.IRON_ORE_DROP_TOOLTIP);
                    if (tier == ModToolTiers.NETHERITE_GOLD) tooltip.add(ModTooltips.GOLD_ORE_DROP_TOOLTIP);
                    if (tier == ModToolTiers.NETHERITE_EMERALD) tooltip.add(ModTooltips.EMERALD_ORE_DROP_TOOLTIP);
                    if (tier == ModToolTiers.NETHERITE_DIAMOND) tooltip.add(ModTooltips.DIAMOND_ORE_DROP_TOOLTIP);
                }
                else
                {
                    tooltip.add(ModTooltips.SHIFT_KEY_TOOLTIP);
                }
            }

            addTooltips(stack, context, tooltip, flag); // Add tooltips from add-ons
        }
    }

    /**
     * Don't override this method, use {@link AdvancedPickaxeItem#customDurabilityBarColor(ItemStack)} to change the custom durability bar color.
     */
    @Internal
    @Override
    public int getBarColor(ItemStack stack)
    {
        int originalColor = super.getBarColor(stack);

        if (customDurabilityBarColor(stack) != null && ConfigHelper.get().getClient().matchingDurabilityBars())
        {
            return Objects.requireNonNull(customDurabilityBarColor(stack).getColor());
        }

        return AdvancedUtil.getDurabilityBarColor(originalColor, stack);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state)
    {
        float originalSpeed = super.getDestroySpeed(stack, state);
        return AdvancedUtil.getDestroySpeed(originalSpeed, stack, state);
    }
}
