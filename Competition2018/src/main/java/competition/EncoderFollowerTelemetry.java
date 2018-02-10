package competition;

import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import xbot.common.properties.DoubleProperty;
import xbot.common.properties.XPropertyManager;

public class EncoderFollowerTelemetry extends EncoderFollower {

	int encoder_offset;
	int encoder_tick_count;
	double wheel_circumference;

	double kp;
	double ki;
	double kd;
	double kv;
	double ka;

	double last_error;
	double heading;

	int segment;
	Trajectory trajectory;

	DoubleProperty positionalError;

	public EncoderFollowerTelemetry(Trajectory traj, XPropertyManager propManager) {
        trajectory = traj;
        positionalError = propManager.createPersistentProperty("EncoderFollower: positional error", 0);
        last_error = positionalError.get();
    }

	@Override
	public void configurePIDVA(double kp, double ki, double kd, double kv, double ka) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
		this.kv = kv;
		this.ka = ka;
	}

	@Override
	public void configureEncoder(int initial_position, int ticks_per_revolution, double wheel_diameter) {
		encoder_offset = initial_position;
		encoder_tick_count = ticks_per_revolution;
		wheel_circumference = Math.PI * wheel_diameter;
	}

	@Override
	public double calculate(int encoder_tick) {
		// Number of Revolutions * Wheel Circumference
		double distance_covered = ((double) (encoder_tick - encoder_offset) / encoder_tick_count) * wheel_circumference;
		if (segment < trajectory.length()) {
			Trajectory.Segment seg = trajectory.get(segment);
			double error = seg.position - distance_covered;
			positionalError.set(error);
			double calculated_value = kp * error + // Proportional
					kd * ((error - last_error) / seg.dt) + // Derivative
					(kv * seg.velocity + ka * seg.acceleration); // V and A Terms
			last_error = error;
			heading = seg.heading;
			segment++;

			return calculated_value;
		} else {
			return 0;
		}
	}
}
