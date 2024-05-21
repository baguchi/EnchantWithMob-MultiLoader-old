package baguchan.enchantwithmob.registry;

import baguchan.enchantwithmob.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

import static baguchan.enchantwithmob.utils.ResourceLocationHelper.modLoc;

public class EwSoundEvents {

    public static final Supplier<SoundEvent> ENCHANTER_IDLE = createEvent("entity.enchanter.idle");
    public static final Supplier<SoundEvent> ENCHANTER_HURT = createEvent("entity.enchanter.hurt");
    public static final Supplier<SoundEvent> ENCHANTER_DEATH = createEvent("entity.enchanter.death");
    public static final Supplier<SoundEvent> ENCHANTER_SPELL = createEvent("entity.enchanter.spell");
    public static final Supplier<SoundEvent> ENCHANTER_ATTACK = createEvent("entity.enchanter.attack");
    public static final Supplier<SoundEvent> ENCHANTER_BEAM = createEvent("entity.enchanter.beam");
    public static final Supplier<SoundEvent> ENCHANTER_BEAM_LOOP = createEvent("entity.enchanter.beam_loop");


    public static void register() {
    }

    private static Supplier<SoundEvent> createEvent(String id) {
        return Services.REGISTRAR.registerObject(modLoc(id), () -> SoundEvent.createVariableRangeEvent(modLoc(id)), BuiltInRegistries.SOUND_EVENT);
    }
}