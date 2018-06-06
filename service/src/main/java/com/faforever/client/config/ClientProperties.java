package com.faforever.client.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@ConfigurationProperties(prefix = "sh-client", ignoreUnknownFields = false)
@JsonIgnoreProperties("trueSkill")
public class ClientProperties {

  private String mainWindowTitle = "Downlord's FAF Client";
  private ForgedAlliance forgedAlliance = new ForgedAlliance();
  private Irc irc = new Irc();
  private Server server = new Server();
  private Vault vault = new Vault();
  private Replay replay = new Replay();
  private Imgur imgur = new Imgur();
  private Api api = new Api();
  private UnitDatabase unitDatabase = new UnitDatabase();
  private Website website = new Website();
  private String translationProjectUrl;
  private String clientConfigUrl;
  private boolean useRemotePreferences;
  private Duration clientConfigConnectTimeout = Duration.ofSeconds(30);
  private boolean showIceAdapterDebugWindow;
  private String statusPageUrl;

  @Data
  public static class ForgedAlliance {
    /**
     * Title of the Forged Alliance window. Required to find the window handle.
     */
    private String windowTitle = "Forged Alliance";

    /**
     * URL to download the ForgedAlliance.exe from.
     */
    private String exeUrl;
  }

  @Data
  public static class Irc {
    private String host;
    private int port = 8167;
    private int reconnectDelay = (int) Duration.ofSeconds(5).toMillis();
  }

  @Data
  public static class Server {
    private String webSocketUrl;
  }

  @Data
  public static class Vault {
    private String baseUrl;
    private String mapDownloadUrlFormat;
    private String mapPreviewUrlFormat;
    private String replayDownloadUrlFormat;
    private String modDownloadUrlFormat;
  }

  @Data
  public static class Replay {
    private String remoteHost;
    private int remotePort = 15000;
    private String replayFileFormat = "%d-%s.fafreplay";
    private String replayFileGlob = "*.fafreplay";
    // TODO this should acutally be reported by the server
    private int watchDelaySeconds = 300;
  }

  @Data
  public static class Imgur {
    private Upload upload = new Upload();

    @Data
    public static class Upload {
      private String baseUrl = "https://api.imgur.com/3/image";
      private String clientId;
      private int maxSize = 2097152;
    }
  }

  @Data
  public static class Website {
    private String baseUrl;
    private String forgotPasswordUrl;
    private String createAccountUrl;
  }

  @Data
  public static class Api {
    private String baseUrl;
    private String clientId;
    private String clientSecret;
    private int maxPageSize = 10_000;
  }

  @Data
  public static class UnitDatabase {
    private String spookiesUrl;
    private String rackOversUrl;
  }
}
