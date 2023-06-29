/*
 * This file is part of Baritone.
 *
 * Baritone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Baritone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Baritone.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.utils.schematic.format.defaults;

import baritone.utils.schematic.StaticSchematic;
import baritone.utils.type.VarInt;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Namespaced;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brady
 * @since 12/27/2019
 */
public final class SpongeSchematic extends StaticSchematic {

    public SpongeSchematic(NBTTagCompound nbt) {
        x = nbt.getInteger("Width");
        y = nbt.getInteger("Height");
        z = nbt.getInteger("Length");
        states = new IBlockState[x][z][y];

        Int2ObjectArrayMap<IBlockState> palette = new Int2ObjectArrayMap<>();
        NBTTagCompound paletteTag = nbt.getCompoundTag("Palette");
        for (String tag : paletteTag.getKeySet()) {
            int index = paletteTag.getInteger(tag);

            SerializedBlockState serializedState = SerializedBlockState.getFromString(tag);
            if (serializedState == null) {
                throw new IllegalArgumentException("Unable to parse palette tag");
            }

            IBlockState state = serializedState.deserialize();
            if (state == null) {
                throw new IllegalArgumentException("Unable to deserialize palette tag");
            }

            palette.put(index, state);
        }

        // BlockData is stored as an NBT byte[], however, the actual data that is represented is a varint[]
        byte[] rawBlockData = nbt.getByteArray("BlockData");
        int[] blockData = new int[x * y * z];
        int offset = 0;
        for (int i = 0; i < blockData.length; i++) {
            if (offset >= rawBlockData.length) {
                throw new IllegalArgumentException("No remaining bytes in BlockData for complete schematic");
            }

            VarInt varInt = VarInt.read(rawBlockData, offset);
            blockData[i] = varInt.getValue();
            offset += varInt.getSize();
        }

        for (int y = 0; y < this.y; y++) {
            for (int z = 0; z < this.z; z++) {
                for (int x = 0; x < this.x; x++) {
                    int index = (y * this.z + z) * this.x + x;
                    IBlockState state = palette.get(blockData[index]);
                    if (state == null) {
                        throw new IllegalArgumentException("Invalid Palette Index " + index);
                    }

                    states[x][z][y] = state;
                }
            }
        }
    }

    private static final class SerializedBlockState {

        private static final Pattern REGEX = Pattern.compile("(?<location>(\\w+:)?\\w+)(\\[(?<properties>(\\w+=\\w+,?)+)])?");

        private final Namespaced namespaced;
        private final Map<String, String> properties;
        private IBlockState blockState;

        private SerializedBlockState(Namespaced namespaced, Map<String, String> properties) {
            this.namespaced = namespaced;
            this.properties = properties;
        }

        private IBlockState deserialize() {
            if (blockState == null) {
                Block block = Block.REGISTRY.getObject(namespaced);
                blockState = block.getDefaultState();

                properties.keySet().stream().sorted(String::compareTo).forEachOrdered(key -> {
                    IProperty<?> property = block.getBlockState().getProperty(key);
                    if (property != null) {
                        blockState = setPropertyValue(blockState, property, properties.get(key));
                    }
                });
            }
            return blockState;
        }

        private static SerializedBlockState getFromString(String s) {
            Matcher m = REGEX.matcher(s);
            if (!m.matches()) {
                return null;
            }

            try {
                String location = m.group("location");
                String properties = m.group("properties");

                Namespaced namespaced = new Namespaced(location);
                Map<String, String> propertiesMap = new HashMap<>();
                if (properties != null) {
                    for (String property : properties.split(",")) {
                        String[] split = property.split("=");
                        propertiesMap.put(split[0], split[1]);
                    }
                }

                return new SerializedBlockState(namespaced, propertiesMap);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private static <T extends Comparable<T>> IBlockState setPropertyValue(IBlockState state, IProperty<T> property, String value) {
            Optional<T> parsed = property.parseValue(value).toJavaUtil();
            if (parsed.isPresent()) {
                return state.withProperty(property, parsed.get());
            } else {
                throw new IllegalArgumentException("Invalid value for property " + property);
            }
        }
    }
}
