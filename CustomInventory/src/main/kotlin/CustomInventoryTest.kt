import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.animal.Horse
import org.spongepowered.api.entity.living.animal.Llama
import org.spongepowered.api.entity.living.animal.Mule
import org.spongepowered.api.entity.living.monster.Slime
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.InteractBlockEvent
import org.spongepowered.api.event.entity.InteractEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.filter.Getter
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.Carrier
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.InventoryArchetypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.*
import org.spongepowered.api.item.inventory.type.CarriedInventory
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

@Plugin(id = "custominventorytest", name = "Custom Inventory Test",
        description = "A plugin to test custom inventories", version = "0.0.0")
class CustomInventoryTest {
    @Listener
    fun onPunchBlock(event: InteractBlockEvent.Primary, @Root player: Player) {
        if (!player.get(Keys.IS_SNEAKING).orElse(false)) {
            return
        }
        event.targetBlock.location.ifPresent { loc ->
            interactCarrier(event, player, loc)
            interactOtherBlock(event, player, loc)
        }
    }

    @Listener
    fun onPunchEntitiy(event: InteractEntityEvent.Primary, @Root player: Player) {
        if (!player.get(Keys.IS_SNEAKING).orElse(false)) {
            return
        }
        val targetEntity = event.targetEntity
        if (targetEntity is Mule) {
            val builder: Inventory.Builder = if (targetEntity.inventory.capacity() <= 2) {
                Inventory.builder().of(InventoryArchetypes.HORSE)
            } else {
                Inventory.builder().of(InventoryArchetypes.HORSE_WITH_CHEST)
            }
            val inventory = builder.property(InventoryTitle.of(Text.of("Custom Mule")))
                    .withCarrier(targetEntity)
                    .build(this)
            var i = 1
            for (slot in inventory.slots<Inventory>()) {
                slot.set(ItemStack.of(ItemTypes.APPLE, i++))
            }
            Sponge.getCauseStackManager().pushCause(player)
            player.openInventory(inventory)
            Sponge.getCauseStackManager().popCause()
            event.isCancelled = true
        } else if (targetEntity is Llama) {
            val builder = if (targetEntity.inventory.capacity() <= 2) {
                Inventory.builder().of(InventoryArchetypes.HORSE)
            } else {
                Inventory.builder().of(InventoryArchetypes.HORSE_WITH_CHEST)
            }
            val inventory = builder.property(InventoryTitle.of(Text.of("Custom Llama")))
                    .withCarrier(targetEntity)
                    .build(this)
            var i = 1
            for (slot in inventory.slots<Inventory>()) {
                slot.set(ItemStack.of(ItemTypes.APPLE, i++))
            }
            Sponge.getCauseStackManager().pushCause(player)
            player.openInventory(inventory)
            Sponge.getCauseStackManager().popCause()
            event.isCancelled = true
        } else if (targetEntity is Horse) {
            val inventory = Inventory.builder().property(InventoryTitle.of(Text.of("Custom Horse")))
                    .withCarrier(targetEntity)
                    .build(this)
            var i = 1
            for (slot in inventory.slots<Inventory>()) {
                slot.set(ItemStack.of(ItemTypes.APPLE, i++))
            }
            Sponge.getCauseStackManager().pushCause(player)
            player.openInventory(inventory)
            Sponge.getCauseStackManager().popCause()
            event.isCancelled = true
        } else if (targetEntity is Slime) {
            val inventory = Inventory.builder().of(InventoryArchetypes.MENU_GRID)
                    .property(InventoryDimension.of(1, 9))
                    .property(InventoryTitle.of(Text.of("Slime Content")))
                    .property(Identifiable())
                    .property(GuiIdProperty(GuiIds.DISPENSER))
                    .build(this)
            val flard = ItemStack.of(ItemTypes.SLIME, 1)
            flard.offer(Keys.DISPLAY_NAME, Text.of("Flard?"))
            for (slot in inventory.slots<Inventory>()) {
                slot.set(flard)
            }
            Sponge.getCauseStackManager().pushCause(player)
            player.openInventory(inventory)
            Sponge.getCauseStackManager().popCause()
        }
    }

    private fun interactOtherBlock(event: InteractBlockEvent.Primary, player: Player, loc: Location<World>) {
        if (loc.blockType == BlockTypes.CRAFTING_TABLE) {
            val inventory = Inventory.builder()
                    .of(InventoryArchetypes.WORKBENCH)
                    .property(InventoryTitle.of(Text.of("Custom Workbench")))
                    .build(this)

            for (slot in inventory.slots<Inventory>()) {
                slot.set(ItemStack.of(ItemTypes.IRON_NUGGET, 1))
            }

            Sponge.getCauseStackManager().pushCause(player)
            player.openInventory(inventory)
            Sponge.getCauseStackManager().popCause()

            event.isCancelled = true
        }
    }

    private fun interactCarrier(event: InteractBlockEvent.Primary, player: Player, loc: Location<World>) {
        loc.tileEntity.ifPresent { titleEntitiy ->
            if (titleEntitiy is Carrier) {
                val myCarrier = BasicCarrier()
                val customInventory = Inventory.builder().from(titleEntitiy.inventory)
                        .property(InventoryTitle.of(Text.of("Custom ", titleEntitiy.inventory.name)))
                        .withCarrier(myCarrier)
                        .build(this)
                myCarrier.init(customInventory)
                Sponge.getCauseStackManager().pushCause(player)
                player.openInventory(customInventory)
                Sponge.getCauseStackManager().popCause()
                event.isCancelled = true
            }
        }
    }

    private val listener: AnnoyingListener = AnnoyingListener()
    private var isRegistered = false

    @Listener
    fun onInit(event: GameInitializationEvent) {
        Sponge.getCommandManager().register(this,
                CommandSpec.builder().executor { source, content ->
                    if (this.isRegistered) {
                        this.isRegistered = false
                        Sponge.getEventManager().unregisterListeners(listener)
                    } else {
                        this.isRegistered = true
                        Sponge.getEventManager().registerListeners(this, listener)
                    }
                    CommandResult.success()
                }.build(), "togglesuperannoyinginventorymessage")
    }

    class AnnoyingListener {
        @Listener
        fun onInventoryClick(event: ClickInventoryEvent, @First player: Player, @Getter("getTargetInventory") container: CarriedInventory<*>) {
            container.getInventoryProperty(Identifiable::class.java).ifPresent { i ->
                player.sendMessage(Text.of("Identifiable Inventory: ", i.value))
            }
            for (transaction in event.transactions) {
                val slot = transaction.slot
                val realSlot = slot.transform()
                val slotClicked = slot.getProperty(SlotIndex::class.java, "slotindex").map { it.value }.orElse(-1)
                player.sendMessage(Text.of("You clicked Slot ", slotClicked, " in ", container.name, "/", realSlot.parent().name))
            }
        }
    }

    class BasicCarrier : Carrier {
        private var inventory: Inventory = Inventory.builder().build(this)

        override fun getInventory(): CarriedInventory<out Carrier> {
            return inventory as CarriedInventory<out Carrier>
        }

        fun init(inventory: Inventory) {
            this.inventory = inventory
        }
    }
}
