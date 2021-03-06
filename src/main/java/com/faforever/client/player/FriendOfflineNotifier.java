package com.faforever.client.player;

import com.faforever.client.audio.AudioService;
import com.faforever.client.i18n.I18n;
import com.faforever.client.notification.NotificationService;
import com.faforever.client.notification.TransientNotification;
import com.faforever.client.preferences.NotificationsPrefs;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.util.IdenticonUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * Displays a notification whenever a friend goes offline (if enabled in settings).
 */
@Component
public class FriendOfflineNotifier implements InitializingBean {

  private final NotificationService notificationService;
  private final I18n i18n;
  private final EventBus eventBus;
  private final AudioService audioService;
  private final PlayerService playerService;
  private final PreferencesService preferencesService;

  @Inject
  public FriendOfflineNotifier(NotificationService notificationService, I18n i18n, EventBus eventBus,
                               AudioService audioService, PlayerService playerService, PreferencesService preferencesService) {
    this.notificationService = notificationService;
    this.i18n = i18n;
    this.eventBus = eventBus;
    this.audioService = audioService;
    this.playerService = playerService;
    this.preferencesService = preferencesService;
  }

  @Override
  public void afterPropertiesSet() {
    eventBus.register(this);
  }

  @Subscribe
  public void onUserOnline(UserOfflineEvent event) {
    NotificationsPrefs notification = preferencesService.getPreferences().getNotification();
    String username = event.getUsername();
    playerService.getPlayerForUsername(username).ifPresent(player -> {
      if (player.getSocialStatus() != SocialStatus.FRIEND) {
        return;
      }

      if (notification.isFriendOfflineSoundEnabled()) {
        audioService.playFriendOfflineSound();
      }

      if (notification.isFriendOfflineToastEnabled()) {
        notificationService.addNotification(
            new TransientNotification(
                i18n.get("friend.nowOfflineNotification.title", username), "",
                IdenticonUtil.createIdenticon(player.getId())
            ));
      }
    });
  }
}
