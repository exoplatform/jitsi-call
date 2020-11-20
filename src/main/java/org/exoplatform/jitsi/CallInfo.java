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

  /** The type. */
  private String       type;

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
   * @param type the type
   * @param moderator the moderator
   * @param participants the participants
   */
  public CallInfo(String owner, String type, String moderator, List<String> participants) {
    this.owner = owner;
    this.type = type;
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
   * Gets the type.
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type the new type
   */
  public void setType(String type) {
    this.type = type;
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
    return "CallInfo [owner=" + owner + ", type=" + type + ", moderator=" + moderator + "]";
  }

}
