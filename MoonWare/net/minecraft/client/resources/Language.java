package net.minecraft.client.resources;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@RequiredArgsConstructor
@Getter
public class Language implements Comparable<Language> {
    private final String code;
    private final String region;
    private final String name;
    private final boolean bidirectional;

    @Override
    public String toString() {
        return String.format("%s (%s)", name, region);
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Language && ((Language)other).getCode().equals(getCode());
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public int compareTo(Language other) {
        return code.compareTo(other.code);
    }
}
