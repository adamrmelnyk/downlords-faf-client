package com.faforever.client.chat;

import com.faforever.client.fx.HostService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.PlayerService;
import com.faforever.client.preferences.ChatPrefs;
import com.faforever.client.preferences.Preferences;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.uploader.ImageUploadService;
import com.faforever.client.user.UserService;
import com.faforever.client.util.TimeService;
import javafx.collections.FXCollections;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChannelTabControllerTest extends AbstractPlainJavaFxTest {

  private static final String CHANNEL_NAME = "#testChannel";

  @Rule
  public TemporaryFolder tempDir = new TemporaryFolder();
  @Mock
  ChatService chatService;
  @Mock
  UserService userService;
  @Mock
  ImageUploadService imageUploadService;
  @Mock
  PlayerService playerService;
  @Mock
  TimeService timeService;
  @Mock
  PreferencesService preferencesService;
  @Mock
  HostService hostService;
  @Mock
  Preferences preferences;
  @Mock
  ChatPrefs chatPrefs;
  @Mock
  I18n i18n;

  private ChannelTabController instance;

  @Before
  public void setUp() throws Exception {
    instance = loadController("channel_tab.fxml");
    instance.chatService = chatService;
    instance.userService = userService;
    instance.imageUploadService = imageUploadService;
    instance.playerService = playerService;
    instance.timeService = timeService;
    instance.preferencesService = preferencesService;
    instance.hostService = hostService;
    instance.i18n = i18n;

    when(preferencesService.getPreferences()).thenReturn(preferences);
    when(preferencesService.getCacheDirectory()).thenReturn(tempDir.getRoot().toPath());
    when(preferences.getTheme()).thenReturn("default");
    when(preferences.getChat()).thenReturn(chatPrefs);
    when(chatPrefs.getZoom()).thenReturn(1d);
    when(userService.getUsername()).thenReturn("junit");

    instance.postConstruct();
  }

  @Test
  public void testGetMessagesWebView() throws Exception {
    assertNotNull(instance.getMessagesWebView());
  }

  @Test
  public void testGetMessageTextField() throws Exception {
    assertNotNull(instance.getMessageTextField());
  }

  @Test
  public void testSetChannelName() throws Exception {
    when(chatService.getChatUsersForChannel(CHANNEL_NAME)).thenReturn(FXCollections.emptyObservableMap());

    instance.setChannelName(CHANNEL_NAME);

    verify(chatService).addChannelUserListListener(eq(CHANNEL_NAME), any());
  }

  @Test
  public void testGetMessageCssClassModerator() throws Exception {
    String playerName = "junit";
    when(chatService.getChatUsersForChannel(CHANNEL_NAME)).thenReturn(FXCollections.emptyObservableMap());

    PlayerInfoBean playerInfoBean = new PlayerInfoBean(playerName);
    playerInfoBean.moderatorForChannelsProperty().set(FXCollections.observableSet(CHANNEL_NAME));
    instance.setChannelName(CHANNEL_NAME);

    assertEquals(instance.getMessageCssClass(playerInfoBean), ChannelTabController.CSS_CLASS_MODERATOR);
  }
}
