package insiders.stormelectronix.memecraftmp.entity.custom;

import insiders.stormelectronix.memecraftmp.animation.ModEntityGeoAnimations;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class PVZAllStarZombieEntity extends ZombieEntity implements IAnimatable {
	public static boolean isAggressive;
	private final AnimationFactory factory = new AnimationFactory(this);

	public PVZAllStarZombieEntity(EntityType<? extends ZombieEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
		return MobEntity.func_233666_p_()
				.createMutableAttribute(Attributes.MAX_HEALTH, 50.0D)
				.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.35D)
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 5.0D)
				.createMutableAttribute(Attributes.ZOMBIE_SPAWN_REINFORCEMENTS);
	}

	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "Running", 5, this::predicate));
		data.addAnimationController(new AnimationController(this, "Attacking", 5, this::attackPredicate));
	}

	protected PlayState attackPredicate(final AnimationEvent event) {
		if (this.isSwingInProgress && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
			event.getController().markNeedsReload();
			event.getController().setAnimation(ModEntityGeoAnimations.PVZ_ZOMBIE_ATTACK_ANIM);
			this.isSwingInProgress = false;
		}
		return PlayState.CONTINUE;
	}

	protected PlayState predicate(final AnimationEvent event) {
		if (event.isMoving()) {
			event.getController().setAnimation(ModEntityGeoAnimations.PVZ_ALLSTAR_RUNNING_ANIM);
			return PlayState.CONTINUE;
		}

		event.getController().setAnimation(ModEntityGeoAnimations.PVZ_ZOMBIE_IDLE_ANIM);
		return PlayState.CONTINUE;
	}

	@Override
	public AnimationFactory getFactory() {
		return factory;
	}

	@Override
	protected int getExperiencePoints(PlayerEntity player) {
		return 5 + this.rand.nextInt(3);
	}

	@Override
	public void livingTick() {
		super.livingTick();

		isAggressive = this.isAggressive();
	}
}
