package competition.subsystems.offboard;

public class OffboardCommsConstants {
    public static final int CAN_ARBID_ROOT = 0x1E040000;
    public static final int CAN_ARBID_ROOT_MASK = 0xFFFF0000;
    public static final int CAN_ARBID_ROOT_AND_SOURCE_MASK = 0xFFFFFF00;

    public static final byte PACKET_TYPE_WHEEL_ODOM = 0x01;
    public static final byte PACKET_TYPE_ORIENTATION = 0x02;
    public static final byte PACKET_TYPE_HEADING = 0x04;
    public static final byte PACKET_TYPE_SET_CURRENT_COMMAND = 0x06;

    public static final byte PACKET_TYPE_DRIVE_POWER_COMMAND = 0x05;
    public static final byte PACKET_TYPE_DRIVE_VEL_COMMAND = 0x09;
    public static final byte PACKET_TYPE_ELEVATOR_POSITION_COMMAND = 0x0A;
    public static final byte PACKET_TYPE_WRIST_COMMAND = 0x0C;
    public static final byte PACKET_TYPE_GRIPPER_CONTROL_COMMAND = 0x0D;
    public static final byte PACKET_TYPE_COMMAND_FINISHED = 0x07;

    public static final byte PACKET_TYPE_DETECTED_CUBE = 0x0B;
}
