package com.happs.ximand.ringcontrol.model.object.command;

public class AuthenticationCommand extends BluetoothCommand<String> {

    private static final byte COMMAND_CODE = 0;

    protected AuthenticationCommand() {
        super(COMMAND_CODE);
    }

    @Override
    public byte[] getCommand() {
        //return getCode() + ;
        return null;
    }

    @Override
    public String getMainContent() {
        throw new UnsupportedOperationException();
    }
}