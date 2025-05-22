package com.example.savsziget;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SavSzigetMod implements ModInitializer {
	private static final int DAMAGE_COOLDOWN = 10;
	private int tickCounter = 0;

	@Override
	public void onInitialize() {
		System.out.println("Sav Sziget Mod initialized!");
		vizSebez();
		csonakCraftolas();


	}

	private void vizSebez() {
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			tickCounter++;
			if (tickCounter >= DAMAGE_COOLDOWN) {
				tickCounter = 0;

				for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
					World world = player.getWorld();
					BlockPos pos = player.getBlockPos();

					if (player.isTouchingWater()) {
						player.damage((ServerWorld)world, world.getDamageSources().lava(), 4.0F);
					}

					if (world.hasRain(pos) && world.isSkyVisible(pos.up())) {
						player.damage((ServerWorld)world, world.getDamageSources().lava(), 1.0F);
					}
				}
			}
		});
	}

	private void csonakCraftolas() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			if (!world.isClient && hand == Hand.MAIN_HAND) {
				if (player.getStackInHand(hand).isOf(Items.OAK_BOAT) ||
						player.getStackInHand(hand).isOf(Items.SPRUCE_BOAT) ||
						player.getStackInHand(hand).isOf(Items.BIRCH_BOAT) ||
						player.getStackInHand(hand).isOf(Items.JUNGLE_BOAT) ||
						player.getStackInHand(hand).isOf(Items.ACACIA_BOAT) ||
						player.getStackInHand(hand).isOf(Items.DARK_OAK_BOAT) ||
						player.getStackInHand(hand).isOf(Items.MANGROVE_BOAT) ||
						player.getStackInHand(hand).isOf(Items.CHERRY_BOAT)) {

					if (player instanceof ServerPlayerEntity) {
						((ServerPlayerEntity) player).sendMessage(Text.literal("Boat use is prohibited!/A csónak használata le van tiltva!"), false);
					}
					return ActionResult.FAIL;
				}
			}
			return ActionResult.PASS;
		});

	}

}
