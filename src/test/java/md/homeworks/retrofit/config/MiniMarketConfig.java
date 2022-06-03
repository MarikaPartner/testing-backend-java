package md.homeworks.retrofit.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

@Config.Sources({"file:src/test/resources/minimarketconfig.properties"})
public interface MiniMarketConfig extends Config {
    MiniMarketConfig miniMarketConfig = ConfigFactory.create(MiniMarketConfig.class);
    String baseURI();
}





