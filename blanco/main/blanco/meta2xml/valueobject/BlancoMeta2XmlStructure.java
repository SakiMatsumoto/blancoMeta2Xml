/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.meta2xml.valueobject;

/**
 * BlancoMeta2Xml �̂Ȃ��ŗ��p�����ValueObject�ł��B
 */
public class BlancoMeta2XmlStructure {
    /**
     * �t�B�[���h�����w�肵�܂��B�K�{���ڂł��B
     *
     * �t�B�[���h: [name]�B
     */
    private String fName;

    /**
     * �p�b�P�[�W�����w�肵�܂��B�K�{���ڂł��B
     *
     * �t�B�[���h: [package]�B
     */
    private String fPackage;

    /**
     * ���̃o�����[�I�u�W�F�N�g�̐������L�ڂ��܂��B
     *
     * �t�B�[���h: [description]�B
     */
    private String fDescription;

    /**
     * �t�@�C���R�����g���w�肵�܂��B
     *
     * �t�B�[���h: [fileDescription]�B
     * �f�t�H���g: ["���̃N���X��blancoValueObject�ɂ�莩����������܂����B"]�B
     */
    private String fFileDescription = "���̃N���X��blancoValueObject�ɂ�莩����������܂����B";

    /**
     * �ϊ���`�t�@�C���̈ʒu���w�肵�܂��B
     *
     * �t�B�[���h: [convertDefFile]�B
     */
    private String fConvertDefFile;

    /**
     * ���̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B
     *
     * �t�B�[���h: [inputFileExt]�B
     * �f�t�H���g: [".xls"]�B
     */
    private String fInputFileExt = ".xls";

    /**
     * �o�̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B
     *
     * �t�B�[���h: [outputFileExt]�B
     * �f�t�H���g: [".xml"]�B
     */
    private String fOutputFileExt = ".xml";

    /**
     * �t�B�[���h [name] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [�t�B�[���h�����w�肵�܂��B�K�{���ڂł��B]�B
     *
     * @param argName �t�B�[���h[name]�ɐݒ肷��l�B
     */
    public void setName(final String argName) {
        fName = argName;
    }

    /**
     * �t�B�[���h [name] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [�t�B�[���h�����w�肵�܂��B�K�{���ڂł��B]�B
     *
     * @return �t�B�[���h[name]����擾�����l�B
     */
    public String getName() {
        return fName;
    }

    /**
     * �t�B�[���h [package] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [�p�b�P�[�W�����w�肵�܂��B�K�{���ڂł��B]�B
     *
     * @param argPackage �t�B�[���h[package]�ɐݒ肷��l�B
     */
    public void setPackage(final String argPackage) {
        fPackage = argPackage;
    }

    /**
     * �t�B�[���h [package] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [�p�b�P�[�W�����w�肵�܂��B�K�{���ڂł��B]�B
     *
     * @return �t�B�[���h[package]����擾�����l�B
     */
    public String getPackage() {
        return fPackage;
    }

    /**
     * �t�B�[���h [description] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [���̃o�����[�I�u�W�F�N�g�̐������L�ڂ��܂��B]�B
     *
     * @param argDescription �t�B�[���h[description]�ɐݒ肷��l�B
     */
    public void setDescription(final String argDescription) {
        fDescription = argDescription;
    }

    /**
     * �t�B�[���h [description] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [���̃o�����[�I�u�W�F�N�g�̐������L�ڂ��܂��B]�B
     *
     * @return �t�B�[���h[description]����擾�����l�B
     */
    public String getDescription() {
        return fDescription;
    }

    /**
     * �t�B�[���h [fileDescription] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [�t�@�C���R�����g���w�肵�܂��B]�B
     *
     * @param argFileDescription �t�B�[���h[fileDescription]�ɐݒ肷��l�B
     */
    public void setFileDescription(final String argFileDescription) {
        fFileDescription = argFileDescription;
    }

    /**
     * �t�B�[���h [fileDescription] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [�t�@�C���R�����g���w�肵�܂��B]�B
     * �f�t�H���g: ["���̃N���X��blancoValueObject�ɂ�莩����������܂����B"]�B
     *
     * @return �t�B�[���h[fileDescription]����擾�����l�B
     */
    public String getFileDescription() {
        return fFileDescription;
    }

    /**
     * �t�B�[���h [convertDefFile] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [�ϊ���`�t�@�C���̈ʒu���w�肵�܂��B]�B
     *
     * @param argConvertDefFile �t�B�[���h[convertDefFile]�ɐݒ肷��l�B
     */
    public void setConvertDefFile(final String argConvertDefFile) {
        fConvertDefFile = argConvertDefFile;
    }

    /**
     * �t�B�[���h [convertDefFile] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [�ϊ���`�t�@�C���̈ʒu���w�肵�܂��B]�B
     *
     * @return �t�B�[���h[convertDefFile]����擾�����l�B
     */
    public String getConvertDefFile() {
        return fConvertDefFile;
    }

    /**
     * �t�B�[���h [inputFileExt] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [���̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B]�B
     *
     * @param argInputFileExt �t�B�[���h[inputFileExt]�ɐݒ肷��l�B
     */
    public void setInputFileExt(final String argInputFileExt) {
        fInputFileExt = argInputFileExt;
    }

    /**
     * �t�B�[���h [inputFileExt] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [���̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B]�B
     * �f�t�H���g: [".xls"]�B
     *
     * @return �t�B�[���h[inputFileExt]����擾�����l�B
     */
    public String getInputFileExt() {
        return fInputFileExt;
    }

    /**
     * �t�B�[���h [outputFileExt] �̒l��ݒ肵�܂��B
     *
     * �t�B�[���h�̐���: [�o�̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B]�B
     *
     * @param argOutputFileExt �t�B�[���h[outputFileExt]�ɐݒ肷��l�B
     */
    public void setOutputFileExt(final String argOutputFileExt) {
        fOutputFileExt = argOutputFileExt;
    }

    /**
     * �t�B�[���h [outputFileExt] �̒l���擾���܂��B
     *
     * �t�B�[���h�̐���: [�o�̓t�@�C���g���q�B���^�f�B���N�g�������ɗ��p����܂��B]�B
     * �f�t�H���g: [".xml"]�B
     *
     * @return �t�B�[���h[outputFileExt]����擾�����l�B
     */
    public String getOutputFileExt() {
        return fOutputFileExt;
    }

    /**
     * ���̃o�����[�I�u�W�F�N�g�̕�����\�����擾���܂��B
     *
     * <P>�g�p��̒���</P>
     * <UL>
     * <LI>�I�u�W�F�N�g�̃V�����[�͈͂̂ݕ����񉻂̏����ΏۂƂȂ�܂��B
     * <LI>�I�u�W�F�N�g���z�Q�Ƃ��Ă���ꍇ�ɂ́A���̃��\�b�h�͎g��Ȃ��ł��������B
     * </UL>
     *
     * @return �o�����[�I�u�W�F�N�g�̕�����\���B
     */
    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.meta2xml.valueobject.BlancoMeta2XmlStructure[");
        buf.append("name=" + fName);
        buf.append(",package=" + fPackage);
        buf.append(",description=" + fDescription);
        buf.append(",fileDescription=" + fFileDescription);
        buf.append(",convertDefFile=" + fConvertDefFile);
        buf.append(",inputFileExt=" + fInputFileExt);
        buf.append(",outputFileExt=" + fOutputFileExt);
        buf.append("]");
        return buf.toString();
    }
}
