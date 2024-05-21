package baguchan.enchantwithmob.mixins;

import baguchan.enchantwithmob.entity.Enchanter;
import baguchan.enchantwithmob.registry.EWEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WoodlandMansionPieces.WoodlandMansionPiece.class, remap = false)
public class WoodlandMansionPieceMixin {
	@Inject(method = "handleDataMarker", at = @At("HEAD"), cancellable = true)
	protected void handleDataMarker(String p_230213_, BlockPos p_230214_, ServerLevelAccessor p_230215_, RandomSource p_230216_, BoundingBox p_230217_, CallbackInfo callbackInfo) {
		if (p_230213_.equals("Enchanter")) {
			Enchanter entity = EWEntityTypes.ENCHANTER.get().create(p_230215_.getLevel());
			entity.setPersistenceRequired();
			entity.moveTo(p_230214_, 0.0F, 0.0F);
            entity.finalizeSpawn(p_230215_, p_230215_.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.STRUCTURE, (SpawnGroupData) null, null);
			p_230215_.addFreshEntityWithPassengers(entity);
			p_230215_.setBlock(p_230214_, Blocks.AIR.defaultBlockState(), 2);
			callbackInfo.cancel();
		}
	}
}
