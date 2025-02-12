/**
 *     Copyright (C) 2019-2020 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.nfvo.v431.model.nsfm;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.ubiqube.etsi.mano.nfvo.v431.model.nsfm.NotificationLink;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Links to resources related to this notification. 
 */
@Schema(description = "Links to resources related to this notification. ")
@Validated


public class AlarmClearedNotificationLinks   {
  @JsonProperty("subscription")
  private NotificationLink subscription = null;

  @JsonProperty("alarm")
  private NotificationLink alarm = null;

  public AlarmClearedNotificationLinks subscription(NotificationLink subscription) {
    this.subscription = subscription;
    return this;
  }

  /**
   * Get subscription
   * @return subscription
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NotificationLink getSubscription() {
    return subscription;
  }

  public void setSubscription(NotificationLink subscription) {
    this.subscription = subscription;
  }

  public AlarmClearedNotificationLinks alarm(NotificationLink alarm) {
    this.alarm = alarm;
    return this;
  }

  /**
   * Get alarm
   * @return alarm
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public NotificationLink getAlarm() {
    return alarm;
  }

  public void setAlarm(NotificationLink alarm) {
    this.alarm = alarm;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlarmClearedNotificationLinks alarmClearedNotificationLinks = (AlarmClearedNotificationLinks) o;
    return Objects.equals(this.subscription, alarmClearedNotificationLinks.subscription) &&
        Objects.equals(this.alarm, alarmClearedNotificationLinks.alarm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscription, alarm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlarmClearedNotificationLinks {\n");
    
    sb.append("    subscription: ").append(toIndentedString(subscription)).append("\n");
    sb.append("    alarm: ").append(toIndentedString(alarm)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
