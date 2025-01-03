package io.github.itskilerluc.familiarfaces.server.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        ForgeConfigSpec.Builder serverConfigBuilder = new ForgeConfigSpec.Builder();
        Server.setupServerConfig(serverConfigBuilder);
        SERVER_SPEC = serverConfigBuilder.build();
    }
    public static class Server {
        public static ForgeConfigSpec.IntValue brushingCooldown;

        private static void setupServerConfig(ForgeConfigSpec.Builder builder) {
            brushingCooldown = builder.comment("The cooldown for brushing armadillos in ticks.")
                    .defineInRange("config.familiar_faces.armadillo_brush_cooldown", 0, 0, Integer.MAX_VALUE);
        }
    }
}
