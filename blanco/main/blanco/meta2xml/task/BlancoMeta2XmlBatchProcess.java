/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.meta2xml.task;

import java.io.IOException;

import blanco.meta2xml.task.valueobject.BlancoMeta2XmlProcessInput;

/**
 * �o�b�`�����N���X [BlancoMeta2XmlBatchProcess]�B
 *
 * <P>�o�b�`�����̌Ăяo����B</P>
 * <code>
 * java -classpath (�N���X�p�X) blanco.meta2xml.task.BlancoMeta2XmlBatchProcess -help
 * </code>
 */
public class BlancoMeta2XmlBatchProcess {
    /**
     * ����I���B
     */
    public static final int END_SUCCESS = 0;

    /**
     * ���ُ͈�I���B�����I��java.lang.IllegalArgumentException�����������ꍇ�B
     */
    public static final int END_ILLEGAL_ARGUMENT_EXCEPTION = 7;

    /**
     * ���o�͗�O�I���B�����I��java.io.IOException�����������ꍇ�B
     */
    public static final int END_IO_EXCEPTION = 8;

    /**
     * �ُ�I���B�o�b�`�̏����J�n�Ɏ��s�����ꍇ�A����ѓ����I��java.lang.Error�܂���java.lang.RuntimeException�����������ꍇ�B
     */
    public static final int END_ERROR = 9;

    /**
     * �R�}���h���C��������s���ꂽ�ۂ̃G���g���|�C���g�ł��B
     *
     * @param args �R���\�[����������p���ꂽ�����B
     */
    public static final void main(final String[] args) {
        final BlancoMeta2XmlBatchProcess batchProcess = new BlancoMeta2XmlBatchProcess();

        // �o�b�`�����̈����B
        final BlancoMeta2XmlProcessInput input = new BlancoMeta2XmlProcessInput();

        boolean isNeedUsage = false;
        boolean isFieldMetadirProcessed = false;

        // �R�}���h���C�������̉�͂������Ȃ��܂��B
        for (int index = 0; index < args.length; index++) {
            String arg = args[index];
            if (arg.startsWith("-verbose=")) {
                input.setVerbose(Boolean.valueOf(arg.substring(9)).booleanValue());
            } else if (arg.startsWith("-metadir=")) {
                input.setMetadir(arg.substring(9));
                isFieldMetadirProcessed = true;
            } else if (arg.startsWith("-targetdir=")) {
                input.setTargetdir(arg.substring(11));
            } else if (arg.startsWith("-tmpdir=")) {
                input.setTmpdir(arg.substring(8));
            } else if (arg.startsWith("-encoding=")) {
                input.setEncoding(arg.substring(10));
            } else if (arg.startsWith("-cache=")) {
                input.setCache(arg.substring(7));
            } else if (arg.equals("-?") || arg.equals("-help")) {
                usage();
                System.exit(END_SUCCESS);
            } else {
                System.out.println("BlancoMeta2XmlBatchProcess: ���̓p�����[�^[" + arg + "]�͖�������܂����B");
                isNeedUsage = true;
            }
        }

        if (isNeedUsage) {
            usage();
        }

        if( isFieldMetadirProcessed == false) {
            System.out.println("BlancoMeta2XmlBatchProcess: �����J�n���s�B���̓p�����[�^[input]�̕K�{�t�B�[���h�l[metadir]�ɒl���ݒ肳��Ă��܂���B");
            System.exit(END_ILLEGAL_ARGUMENT_EXCEPTION);
        }

        int retCode = batchProcess.execute(input);

        // �I���R�[�h��߂��܂��B
        // �����ӁFSystem.exit()���Ăяo���Ă���_�ɒ��ӂ��Ă��������B
        System.exit(retCode);
    }

    /**
     * ��̓I�ȃo�b�`�������e���L�q���邽�߂̃��\�b�h�ł��B
     *
     * ���̃��\�b�h�Ɏ��ۂ̏������e���L�q���܂��B
     *
     * @param input �o�b�`�����̓��̓p�����[�^�B
     * @return �o�b�`�����̏I���R�[�h�BEND_SUCCESS, END_ILLEGAL_ARGUMENT_EXCEPTION, END_IO_EXCEPTION, END_ERROR �̂����ꂩ�̒l��߂��܂��B
     * @throws IOException ���o�͗�O�����������ꍇ�B
     * @throws IllegalArgumentException ���͒l�ɕs�������������ꍇ�B
     */
    public int process(final BlancoMeta2XmlProcessInput input) throws IOException, IllegalArgumentException {
        // ���̓p�����[�^���`�F�b�N���܂��B
        validateInput(input);

        // ���̉ӏ��ŃR���p�C���G���[����������ꍇ�ABlancoMeta2XmlProcess�C���^�t�F�[�X���������� blanco.meta2xml.task�p�b�P�[�W�� BlancoMeta2XmlProcessImpl�N���X���쐬���邱�Ƃɂ������ł���ꍇ������܂��B
        final BlancoMeta2XmlProcess process = new BlancoMeta2XmlProcessImpl();

        // �����̖{�̂����s���܂��B
        final int retCode = process.execute(input);

        return retCode;
    }

    /**
     * �N���X���C���X�^���X�����ăo�b�`�����s����ۂ̃G���g���|�C���g�ł��B
     *
     * ���̃��\�b�h�͉��L�̎d�l��񋟂��܂��B
     * <ul>
     * <li>���\�b�h�̓��̓p�����[�^�̓��e�`�F�b�N�B
     * <li>IllegalArgumentException, RuntimeException, Error�Ȃǂ̗�O��catch���Ė߂�l�ւƕϊ��B
     * </ul>
     *
     * @param input �o�b�`�����̓��̓p�����[�^�B
     * @return �o�b�`�����̏I���R�[�h�BEND_SUCCESS, END_ILLEGAL_ARGUMENT_EXCEPTION, END_IO_EXCEPTION, END_ERROR �̂����ꂩ�̒l��߂��܂��B
     * @throws IllegalArgumentException ���͒l�ɕs�������������ꍇ�B
     */
    public final int execute(final BlancoMeta2XmlProcessInput input) throws IllegalArgumentException {
        try {
            // �o�b�`�����̖{�̂����s���܂��B
            int retCode = process(input);

            return retCode;
        } catch (IllegalArgumentException ex) {
            System.out.println("BlancoMeta2XmlBatchProcess: ���͗�O���������܂����B�o�b�`�����𒆒f���܂��B:" + ex.toString());
            // ���ُ͈�I���B
            return END_ILLEGAL_ARGUMENT_EXCEPTION;
        } catch (IOException ex) {
            System.out.println("BlancoMeta2XmlBatchProcess: ���o�͗�O���������܂����B�o�b�`�����𒆒f���܂��B:" + ex.toString());
            // ���ُ͈�I���B
            return END_IO_EXCEPTION;
        } catch (RuntimeException ex) {
            System.out.println("BlancoMeta2XmlBatchProcess: �����^�C����O���������܂����B�o�b�`�����𒆒f���܂��B:" + ex.toString());
            ex.printStackTrace();
            // �ُ�I���B
            return END_ERROR;
        } catch (Error er) {
            System.out.println("BlancoMeta2XmlBatchProcess: �����^�C���G���[���������܂����B�o�b�`�����𒆒f���܂��B:" + er.toString());
            er.printStackTrace();
            // �ُ�I���B
            return END_ERROR;
        }
    }

    /**
     * ���̃o�b�`�����N���X�̎g�����̐�����W���o�͂Ɏ������߂̃��\�b�h�ł��B
     */
    public static final void usage() {
        System.out.println("BlancoMeta2XmlBatchProcess: Usage:");
        System.out.println("  java blanco.meta2xml.task.BlancoMeta2XmlBatchProcess -verbose=�l1 -metadir=�l2 -targetdir=�l3 -tmpdir=�l4 -encoding=�l5 -cache=�l6");
        System.out.println("    -verbose");
        System.out.println("      ����[verbose���[�h�œ��삳���邩�ǂ����B]");
        System.out.println("      �^[�^�U]");
        System.out.println("      �f�t�H���g�l[false]");
        System.out.println("    -metadir");
        System.out.println("      ����[���^�f�B���N�g���Bxls�t�@�C���̊i�[��܂��� xml�t�@�C���̊i�[����w�肵�܂��B]");
        System.out.println("      �^[������]");
        System.out.println("      �K�{�p�����[�^");
        System.out.println("    -targetdir");
        System.out.println("      ����[�o�͐�t�H���_���w�肵�܂��B���w��̏ꍇ�ɂ̓J�����g������blanco��p���܂��B]");
        System.out.println("      �^[������]");
        System.out.println("      �f�t�H���g�l[blanco]");
        System.out.println("    -tmpdir");
        System.out.println("      ����[�e���|�����f�B���N�g�����w�肵�܂��B���w��̏ꍇ�ɂ̓J�����g������tmp��p���܂��B]");
        System.out.println("      �^[������]");
        System.out.println("      �f�t�H���g�l[tmp]");
        System.out.println("    -encoding");
        System.out.println("      ����[������������\�[�X�t�@�C���̕����G���R�[�f�B���O���w�肵�܂��B]");
        System.out.println("      �^[������]");
        System.out.println("    -cache");
        System.out.println("      ����[��`�����^�t�@�C�����璆��XML�t�@�C���ւ̕ϊ����L���b�V���ōς܂����ǂ����̃t���O�B]");
        System.out.println("      �^[������]");
        System.out.println("      �f�t�H���g�l[false]");
        System.out.println("    -? , -help");
        System.out.println("      ����[�g������\�����܂��B]");
    }

    /**
     * ���̃o�b�`�����N���X�̓��̓p�����[�^�̑Ó����`�F�b�N�����{���邽�߂̃��\�b�h�ł��B
     *
     * @param input �o�b�`�����̓��̓p�����[�^�B
     * @throws IllegalArgumentException ���͒l�ɕs�������������ꍇ�B
     */
    public void validateInput(final BlancoMeta2XmlProcessInput input) throws IllegalArgumentException {
        if (input == null) {
            throw new IllegalArgumentException("BlancoBatchProcessBatchProcess: �����J�n���s�B���̓p�����[�^[input]��null���^�����܂����B");
        }
        if (input.getMetadir() == null) {
            throw new IllegalArgumentException("BlancoMeta2XmlBatchProcess: �����J�n���s�B���̓p�����[�^[input]�̕K�{�t�B�[���h�l[metadir]�ɒl���ݒ肳��Ă��܂���B");
        }
    }
}
