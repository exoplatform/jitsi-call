package org.exoplatform.jitsi;

import java.util.List;

/**
 * CallInfo is used to store basic information about the call.
 * This can be used to define a user/group who will be owner of recording. 
 * ( moderator or owner ) 
 */
public class CallInfo {

  /** The owner. */
  private String       owner;

  /** The group. */
  private boolean      group;

  /** The moderator. */
  private String       moderator;

  /** The participants. */
  private List<String> participants;

  /**
   * Instantiates a new call info.
   */
  public CallInfo() {

  }

  /**
   * Instantiates a new call info.
   *
   * @param owner the owner
   * @param group the group
   * @param moderator the moderator
   * @param participants the participants
   */
  public CallInfo(String owner, boolean group, String moderator, List<String> participants) {
    this.owner = owner;
    this.group = group;
    this.moderator = moderator;
    this.participants = participants;
  }

  /**
   * Gets the owner.
   *
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }

  /**
   * Sets the owner.
   *
   * @param owner the new owner
   */
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   * Checks if is group.
   *
   * @return true, if is group
   */
  public boolean isGroup() {
    return group;
  }

  /**
   * Sets the group.
   *
   * @param group the new group
   */
  public void setGroup(boolean group) {
    this.group = group;
  }

  /**
   * Gets the moderator.
   *
   * @return the moderator
   */
  public String getModerator() {
    return moderator;
  }

  /**
   * Sets the moderator.
   *
   * @param moderator the new moderator
   */
  public void setModerator(String moderator) {
    this.moderator = moderator;
  }

  /**
   * Gets the participants.
   *
   * @return the participants
   */
  public List<String> getParticipants() {
    return participants;
  }

  /**
   * Sets the participants.
   *
   * @param participants the new participants
   */
  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "CallInfo [owner=" + owner + ", group=" + group + ", moderator=" + moderator + "]";
  }

}
