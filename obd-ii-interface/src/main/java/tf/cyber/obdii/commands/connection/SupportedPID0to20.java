package tf.cyber.obdii.commands.connection;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.commands.engine.*;
import tf.cyber.obdii.commands.fuel.*;
import tf.cyber.obdii.commands.vehicle.AuxillaryInputStatus;
import tf.cyber.obdii.commands.vehicle.OBDStandardCompliance;
import tf.cyber.obdii.commands.vehicle.RuntimeSinceEngineStart;
import tf.cyber.obdii.commands.vehicle.VehicleSpeed;
import tf.cyber.obdii.util.ByteUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SupportedPID0to20 extends OBD2Command<List<Class<OBD2Command<?>>>> {
    private static final Class<OBD2Command<?>>[][] PID_INDEX = new Class[][]{
            {null, null, FuelSystemStatus.class, CalculatedEngineLoad.class},
            {EngineCoolantTemperature.class, ShortTermFuelTrimBank1.class, LongTermFuelTrimBank1.class, ShortTermFuelTrimBank2.class},
            {LongTermFuelTrimBank2.class, FuelPressure.class, IntakeManifoldAbsolutePressure.class, EngineSpeed.class},
            {VehicleSpeed.class, TimingAdvance.class, IntakeAirTemperature.class, MassAirFlowRate.class},
            {ThrottlePosition.class, CommandedSecondaryAirStatus.class, OxygenSensorsPresent.class, OxygenSensor1Voltage.class},
            {OxygenSensor2Voltage.class, OxygenSensor3Voltage.class, OxygenSensor4Voltage.class, OxygenSensor5Voltage.class},
            {OxygenSensor6Voltage.class, OxygenSensor7Voltage.class, OxygenSensor8Voltage.class, OBDStandardCompliance.class},
            {OxygenSensorsPresentFourBanks.class, AuxillaryInputStatus.class, RuntimeSinceEngineStart.class, SupportedPID21to40.class}
    };

    @Override
    public String command() {
        return "01 00";
    }

    @Override
    public List<Class<OBD2Command<?>>> result() {
        List<Class<OBD2Command<?>>> supportedCommands = new LinkedList<>();

        String [] bytesStr = rawData.split(" ");
        String str = bytesStr[bytesStr.length - 4] + bytesStr[bytesStr.length - 3]
                + bytesStr[bytesStr.length - 2] + bytesStr[bytesStr.length - 1];

        String[] chars = str.split("");

        for (int c = 0; c < chars.length; c++) {
            int b = Integer.parseInt(chars[c], 16);

            List<Boolean> mask = Arrays.asList(ByteUtils.byteToBooleanFourBits(b));
            Collections.reverse(mask);

            for (int i = 0; i < mask.size(); i++) {
                supportedCommands.add(PID_INDEX[c][i]);
            }
        }

        return supportedCommands;
    }

    @Override
    public String getFriendlyName() {
        return "Fetch Supported PIDs (0 to 20)";
    }
}
