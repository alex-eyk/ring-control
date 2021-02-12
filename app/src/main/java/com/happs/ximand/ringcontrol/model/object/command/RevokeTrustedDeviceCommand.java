package com.happs.ximand.ringcontrol.model.object.command;

public class RevokeTrustedDeviceCommand extends BluetoothCommand<String> {

    private static final byte REVOKE_TRUSTED_DEVICE_COMMAND_CODE = 2;

    protected RevokeTrustedDeviceCommand() {
        super(REVOKE_TRUSTED_DEVICE_COMMAND_CODE);
    }

    @Override
    public byte[] getCommand() {
        return null;
    }

    @Override
    public String getMainContent() {
        throw new UnsupportedOperationException();
    }
}