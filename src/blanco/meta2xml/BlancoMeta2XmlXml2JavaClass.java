/*
 * blanco Framework
 * Copyright (C) 2004-2009 IGA Tosiki
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package blanco.meta2xml;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import blanco.cg.BlancoCgObjectFactory;
import blanco.cg.transformer.BlancoCgTransformerFactory;
import blanco.cg.valueobject.BlancoCgClass;
import blanco.cg.valueobject.BlancoCgField;
import blanco.cg.valueobject.BlancoCgMethod;
import blanco.cg.valueobject.BlancoCgSourceFile;
import blanco.commons.util.BlancoJavaSourceUtil;
import blanco.commons.util.BlancoStringUtil;
import blanco.commons.util.BlancoXmlUtil;
import blanco.meta2xml.valueobject.BlancoMeta2XmlStructure;

/**
 * blancoValueObject�̎傽��N���X�B
 * 
 * blancoValueObject��\������XML�t�@�C������ Java�\�[�X�R�[�h�������������܂��B
 * 
 * @author IGA Tosiki
 */
public class BlancoMeta2XmlXml2JavaClass {
    /**
     * �����I�ɗ��p����blancoCg�p�t�@�N�g���B
     */
    private BlancoCgObjectFactory fCgFactory = null;

    /**
     * �����I�ɗ��p����blancoCg�p�\�[�X�t�@�C�����B
     */
    private BlancoCgSourceFile fCgSourceFile = null;

    /**
     * �����I�ɗ��p����blancoCg�p�N���X���B
     */
    private BlancoCgClass fCgClass = null;

    /**
     * ������������\�[�X�t�@�C���̕����G���R�[�f�B���O�B
     */
    private String fEncoding = null;

    public void setEncoding(final String argEncoding) {
        fEncoding = argEncoding;
    }

    /**
     * ValueObject��\������XML�t�@�C������ Java�\�[�X�R�[�h�������������܂��B
     * 
     * @param metaXmlSourceFile
     *            ValueObject�Ɋւ��郁�^��񂪊܂܂�Ă���XML�t�@�C��
     * @param directoryTarget
     *            �\�[�X�R�[�h������f�B���N�g��
     * @throws IOException
     *             ���o�͗�O�����������ꍇ
     */
    public void process(final File metaXmlSourceFile, final File directoryTarget)
            throws IOException {

        final DOMResult result = BlancoXmlUtil
                .transformFile2Dom(metaXmlSourceFile);

        final Node rootNode = result.getNode();
        if (rootNode instanceof Document) {
            // ���ꂪ����n�B�h�L�������g���[�g���擾
            final Document rootDocument = (Document) rootNode;
            final NodeList listSheet = rootDocument
                    .getElementsByTagName("sheet");
            final int sizeListSheet = listSheet.getLength();
            for (int index = 0; index < sizeListSheet; index++) {
                final Element elementCommon = BlancoXmlUtil.getElement(
                        listSheet.item(index), "blancometa2xml-process-common");
                if (elementCommon == null) {
                    // common�������ꍇ�ɂ̓X�L�b�v���܂��B
                    continue;
                }

                final String name = BlancoXmlUtil.getTextContent(elementCommon,
                        "name");
                if (name == null || name.trim().length() == 0) {
                    continue;
                }

                expandSheet(elementCommon, directoryTarget);
            }
        }
    }

    /**
     * �V�[�g��W�J���܂��B
     * 
     * @param elementCommon
     *            ���ݏ������Ă���Common�m�[�h
     * @param directoryTarget
     *            �o�͐�t�H���_�B
     */
    private void expandSheet(final Element elementCommon,
            final File directoryTarget) {
        final BlancoMeta2XmlStructure processStructure = new BlancoMeta2XmlStructure();
        processStructure.setName(BlancoXmlUtil.getTextContent(elementCommon,
                "name"));
        processStructure.setPackage(BlancoXmlUtil.getTextContent(elementCommon,
                "package"));
        if (processStructure.getPackage() == null
                || processStructure.getPackage().trim().length() == 0) {
            throw new IllegalArgumentException("���^�t�@�C��-XML�ϊ�������` �N���X��["
                    + processStructure.getName() + "]�̃p�b�P�[�W���w�肳��Ă��܂���B");
        }

        if (BlancoXmlUtil.getTextContent(elementCommon, "description") != null) {
            processStructure.setDescription(BlancoXmlUtil.getTextContent(
                    elementCommon, "description"));
        }
        if (BlancoXmlUtil.getTextContent(elementCommon, "fileDescription") != null) {
            processStructure.setFileDescription(BlancoXmlUtil.getTextContent(
                    elementCommon, "fileDescription"));
        }

        processStructure.setConvertDefFile(BlancoXmlUtil.getTextContent(
                elementCommon, "convertDefFile"));
        if (processStructure.getConvertDefFile() == null
                || processStructure.getConvertDefFile().trim().length() == 0) {
            throw new IllegalArgumentException("���^�t�@�C��-XML�ϊ�������` �N���X��["
                    + processStructure.getName() + "]�̕ϊ���`�t�@�C�����w�肳��Ă��܂���B");
        }

        if (BlancoXmlUtil.getTextContent(elementCommon, "inputFileExt") != null) {
            processStructure.setInputFileExt(BlancoXmlUtil.getTextContent(
                    elementCommon, "inputFileExt"));
        }
        if (BlancoXmlUtil.getTextContent(elementCommon, "outputFileExt") != null) {
            processStructure.setOutputFileExt(BlancoXmlUtil.getTextContent(
                    elementCommon, "outputFileExt"));
        }

        expandJavaSource(processStructure, directoryTarget);
    }

    /**
     * ���W���ꂽ�������ɁAJava�\�[�X�R�[�h���o�͂��܂��B
     * 
     * @param processStructure
     *            �����\��
     * @param directoryTarget
     *            �o�͐�t�H���_�B
     */
    private void expandJavaSource(
            final BlancoMeta2XmlStructure processStructure,
            final File directoryTarget) {
        // �]���ƌ݊������������邽�߁A/main�T�u�t�H���_�ɏo�͂��܂��B
        final File fileBlancoMain = new File(directoryTarget.getAbsolutePath()
                + "/main");

        fCgFactory = BlancoCgObjectFactory.getInstance();
        fCgSourceFile = fCgFactory.createSourceFile(processStructure
                .getPackage(), "���̃\�[�X�R�[�h�� blanco Framework�ɂ���Ď�����������Ă��܂��B");
        fCgSourceFile.setEncoding(fEncoding);
        if (processStructure.getFileDescription() != null) {
            fCgSourceFile.getLangDoc().getDescriptionList().add(
                    processStructure.getFileDescription());
        }

        fCgClass = fCgFactory.createClass(processStructure.getName(),
                BlancoStringUtil.null2Blank(processStructure.getDescription()));
        fCgSourceFile.getClassList().add(fCgClass);

        fCgSourceFile.getImportList().add("java.io.BufferedInputStream");
        fCgSourceFile.getImportList().add("java.io.BufferedOutputStream");
        fCgSourceFile.getImportList().add("java.io.FileInputStream");
        fCgSourceFile.getImportList().add("java.io.FileOutputStream");
        fCgSourceFile.getImportList().add("java.io.IOException");
        fCgSourceFile.getImportList().add("java.io.InputStream");
        fCgSourceFile.getImportList().add("java.io.OutputStream");
        fCgSourceFile.getImportList().add(
                "javax.xml.transform.TransformerException");
        fCgSourceFile.getImportList().add(
                "blanco.commons.calc.parser.BlancoCalcParser");

        {
            final BlancoCgField field = fCgFactory.createField(
                    "fCacheMeta2Xml", "boolean",
                    "��`�����^�t�@�C�����璆��XML�t�@�C���ւ̕ϊ����L���b�V���ōς܂����ǂ����̃t���O�B");
            fCgClass.getFieldList().add(field);
            field.setAccess("protected");
            field.setDefault("false");
        }

        {
            final BlancoCgField field = fCgFactory.createField(
                    "fCacheMeta2XmlCount", "int",
                    "��`�����^�t�@�C�����璆��XML�t�@�C���ւ̕ϊ����L���b�V���ōς܂����񐔁B");
            fCgClass.getFieldList().add(field);
            field.setAccess("protected");
            field.setDefault("0");
        }

        {
            final BlancoCgField field = fCgFactory.createField(
                    "fCacheMetaDefXml", "byte[]",
                    "�N���X���[�_����̒�`���\��XML�t�@�C���̓Ǎ��񐔂����炷���߂̃L���b�V���B");
            fCgClass.getFieldList().add(field);
            field.setAccess("protected");
            field.setDefault("null");
        }

        {
            final BlancoCgMethod method = fCgFactory.createMethod(
                    "setCacheMeta2Xml",
                    "��`�����^�t�@�C�����璆��XML�t�@�C���ւ̕ϊ����L���b�V���ōς܂����ǂ����̃t���O���w�肵�܂��B");
            fCgClass.getMethodList().add(method);

            method.getParameterList().add(
                    fCgFactory.createParameter("argCacheMeta2Xml", "boolean",
                            "��`�����^�t�@�C�����璆��XML�t�@�C���ւ̕ϊ����L���b�V���ōς܂����ǂ����B"));

            final List<java.lang.String> listLine = method.getLineList();
            listLine.add("fCacheMeta2Xml = argCacheMeta2Xml;");
        }

        {
            final BlancoCgMethod methodProcess1 = fCgFactory.createMethod(
                    "process", "Excel�t�@�C���̃X�g���[����XML�t�@�C���̃X�g���[���ɕϊ����܂��B");
            fCgClass.getMethodList().add(methodProcess1);
            methodProcess1.getLangDoc().getDescriptionList().add(
                    "��`�t�@�C���͓����I�Ƀp�X��ێ����Ă��܂��B");
            methodProcess1.getParameterList().add(
                    fCgFactory.createParameter("inStreamMetaSource",
                            "java.io.InputStream", "���^�t�@�C���̓��̓X�g���[���B"));
            methodProcess1.getParameterList().add(
                    fCgFactory.createParameter("outStreamTarget",
                            "java.io.OutputStream", "XML�t�@�C���̏o�̓X�g���[���B"));
            methodProcess1.getThrowList().add(
                    fCgFactory.createException("java.io.IOException",
                            "���o�͗�O�����������ꍇ�B"));
            methodProcess1.getThrowList().add(
                    fCgFactory.createException(
                            "javax.xml.transform.TransformerException",
                            "XML�ϊ���O�����������ꍇ�B"));
            final List<java.lang.String> listLine = methodProcess1
                    .getLineList();
            listLine.add("if (inStreamMetaSource == null) {");

            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: inStreamMetaSource is null.\");");
            listLine.add("}");
            listLine.add("if (outStreamTarget == null) {");
            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: outStreamTarget is null.\");");
            listLine.add("}");
            listLine.add("");
            listLine.add("if (fCacheMetaDefXml == null) {");
            listLine.add("// ���̃N���X���g�Ƃ��Ȃ��N���X���[�_����XML�ݒ�t�@�C���̃��[�h�������Ȃ��܂��B");
            listLine
                    .add("final InputStream meta2xmlStream = getClass().getClassLoader().getResourceAsStream(\""
                            + processStructure.getConvertDefFile() + "\");");
            listLine.add("if (meta2xmlStream == null) {");
            listLine
                    .add("throw new IllegalArgumentException(\""
                            + processStructure.getName() + ": ���\�[�X["
                            + processStructure.getConvertDefFile()
                            + "]�̎擾�Ɏ��s���܂���.\");");
            listLine.add("}");
            fCgSourceFile.getImportList().add("java.io.ByteArrayOutputStream");
            fCgSourceFile.getImportList().add("java.io.ByteArrayInputStream");
            listLine
                    .add("final ByteArrayOutputStream outStream = new ByteArrayOutputStream();");
            listLine.add("final byte[] bufWrk = new byte[8192];");
            listLine.add("for (;;) {");
            listLine.add("final int readLength = meta2xmlStream.read(bufWrk);");
            listLine.add("if (readLength <= 0) {");
            listLine.add("break;");
            listLine.add("}");
            listLine.add("outStream.write(bufWrk, 0, readLength);");
            listLine.add("}");
            listLine.add("outStream.flush();");
            listLine.add("meta2xmlStream.close();");
            listLine.add("fCacheMetaDefXml = outStream.toByteArray();");

            listLine.add("}");
            listLine.add("");
            listLine
                    .add("InputStream inStreamDef = new ByteArrayInputStream(fCacheMetaDefXml);");
            listLine.add("try {");
            listLine
                    .add("new BlancoCalcParser().process(inStreamDef, inStreamMetaSource, outStreamTarget);");
            listLine.add("} finally {");
            listLine.add("if (inStreamDef != null) {");
            listLine.add("inStreamDef.close();");
            listLine.add("}");
            listLine.add("}");
        }

        {
            final BlancoCgMethod methodProcess2 = fCgFactory.createMethod(
                    "process", "Excel�t�@�C����XML�t�@�C���ɕϊ����܂��B");
            fCgClass.getMethodList().add(methodProcess2);
            methodProcess2.getParameterList().add(
                    fCgFactory.createParameter("fileMeta", "java.io.File",
                            "���^�t�@�C���̓��̓t�@�C���B"));
            methodProcess2.getParameterList().add(
                    fCgFactory.createParameter("fileOutput", "java.io.File",
                            "XML�t�@�C���̏o�́B"));
            methodProcess2.getThrowList().add(
                    fCgFactory.createException("java.io.IOException",
                            "���o�͗�O�����������ꍇ�B"));
            methodProcess2.getThrowList().add(
                    fCgFactory.createException(
                            "javax.xml.transform.TransformerException",
                            "XML�ϊ���O�����������ꍇ�B"));
            final List<java.lang.String> listLine = methodProcess2
                    .getLineList();

            listLine.add("if (fileMeta == null) {");
            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: fileMeta is null.\");");
            listLine.add("}");
            listLine.add("if (fileOutput == null) {");
            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: fileOutput is null.\");");
            listLine.add("}");
            listLine.add("if (fileMeta.exists() == false) {");
            listLine
                    .add("throw new IllegalArgumentException(\""
                            + processStructure.getName()
                            + ": Invalid argument: file file [\" + fileMeta.getAbsolutePath() + \"] not found.\");");
            listLine.add("}");
            listLine.add("");
            listLine
                    .add("if (fCacheMeta2Xml && fileMeta.lastModified() < fileOutput.lastModified()) {");
            listLine.add("// �L���b�V���𗘗p���āA�������X�L�b�v���܂��B");
            listLine.add("fCacheMeta2XmlCount++;");
            listLine.add("return;");
            listLine.add("}");
            listLine.add("");
            listLine.add("InputStream inStream = null;");
            listLine.add("OutputStream outStream = null;");
            listLine.add("try {");
            listLine
                    .add("inStream = new BufferedInputStream(new FileInputStream(fileMeta), 8192);");
            listLine
                    .add("outStream = new BufferedOutputStream(new FileOutputStream(fileOutput), 8192);");
            listLine.add("");
            listLine.add("// �X�g���[���̏������ł����̂ŁA���ۂ̏����������Ȃ��܂��B");
            listLine.add("process(inStream, outStream);");
            listLine.add("");
            listLine.add("outStream.flush();");
            listLine.add("} finally {");
            listLine.add("if (inStream != null) {");
            listLine.add("inStream.close();");
            listLine.add("}");
            listLine.add("if (outStream != null) {");
            listLine.add("outStream.close();");
            listLine.add("}");
            listLine.add("}");
        }

        {
            final BlancoCgMethod methodProcessDirectory = fCgFactory
                    .createMethod("processDirectory",
                            "�w��f�B���N�g������Excel�t�@�C����XML�t�@�C���ɕϊ����܂��B");
            fCgClass.getMethodList().add(methodProcessDirectory);
            methodProcessDirectory.getLangDoc().getDescriptionList().add(
                    "�w�肳�ꂽ�t�H���_���̊g���q[" + processStructure.getInputFileExt()
                            + "]�̃t�@�C�����������܂��B<br>");
            methodProcessDirectory.getLangDoc().getDescriptionList().add(
                    "���������f�[�^�� ���Ƃ̃t�@�C�����Ɋg���q["
                            + processStructure.getOutputFileExt()
                            + "]��t�^�����t�@�C���֕ۑ����܂��B");
            methodProcessDirectory.getParameterList().add(
                    fCgFactory.createParameter("fileMetadir", "java.io.File",
                            "���^�t�@�C�����i�[����Ă�����̓f�B���N�g���B"));
            methodProcessDirectory.getParameterList().add(
                    fCgFactory.createParameter("targetDirectory",
                            "java.lang.String", "�o�̓f�B���N�g���B"));
            methodProcessDirectory.getThrowList().add(
                    fCgFactory.createException("java.io.IOException",
                            "���o�͗�O�����������ꍇ�B"));
            methodProcessDirectory.getThrowList().add(
                    fCgFactory.createException(
                            "javax.xml.transform.TransformerException",
                            "XML�ϊ���O�����������ꍇ�B"));
            final List<java.lang.String> listLine = methodProcessDirectory
                    .getLineList();

            listLine.add("System.out.println(\"m2x: begin.\");");
            listLine.add("final long startMills = System.currentTimeMillis();");
            listLine.add("long totalFileCount = 0;");
            listLine.add("long totalFileBytes = 0;");
            listLine.add("");
            listLine.add("if (fileMetadir == null) {");
            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: fileMetadir is null.\");");
            listLine.add("}");
            listLine.add("if (targetDirectory == null) {");
            listLine.add("throw new IllegalArgumentException(\""
                    + processStructure.getName()
                    + ": Invalid argument: targetDirectory is null.\");");
            listLine.add("}");
            listLine.add("if (fileMetadir.exists() == false) {");
            listLine
                    .add("throw new IllegalArgumentException(\""
                            + processStructure.getName()
                            + ": Invalid argument: file [\" + fileMetadir.getAbsolutePath() + \"] not found.\");");
            listLine.add("}");
            listLine
                    .add("final File fileTargetDirectory = new File(targetDirectory);");
            listLine.add("if (fileTargetDirectory.exists() == false) {");
            listLine.add("// �o�͐�f�B���N�g�������݂��Ȃ��̂ŁA���O�ɍ쐬���܂��B");
            listLine.add("fileTargetDirectory.mkdirs();");
            listLine.add("}");
            listLine.add("");
            listLine.add("// �w�肳�ꂽ�f�B���N�g���̃t�@�C���ꗗ���擾���܂��B");
            listLine.add("final File[] fileMeta = fileMetadir.listFiles();");
            listLine.add("if (fileMeta == null) {");
            listLine
                    .add("throw new IllegalArgumentException(\"BlancoMeta2XmlProcessMeta2Xml: list directory [\" + fileMetadir.getAbsolutePath() + \"] is failed.\");");
            listLine.add("}");
            listLine
                    .add("for (int index = 0; index < fileMeta.length; index++) {");
            listLine.add("if (fileMeta[index].getName().endsWith(\""
                    + BlancoJavaSourceUtil
                            .escapeStringAsJavaSource(processStructure
                                    .getInputFileExt()) + "\") == false) {");
            listLine.add("// �t�@�C���̊g���q���������ׂ����̂Ƃ͈قȂ邽�ߏ������X�L�b�v���܂��B�B");
            listLine.add("continue;");
            listLine.add("}");
            listLine.add("");

            listLine
                    .add("if (progress(index + 1, fileMeta.length, fileMeta[index].getName()) == false) {");
            listLine.add("// �i���\�����珈�����f�̎w���������̂ŁA�������f���܂��B");
            listLine.add("break;");
            listLine.add("}");

            listLine.add("");
            listLine.add("try {");
            listLine.add("totalFileCount++;");
            listLine.add("totalFileBytes += fileMeta[index].length();");
            listLine
                    .add("process(fileMeta[index], new File(targetDirectory + \"/\" + fileMeta[index].getName() + \""
                            + BlancoJavaSourceUtil
                                    .escapeStringAsJavaSource(processStructure
                                            .getOutputFileExt()) + "\"));");
            listLine.add("} catch (Exception ex) {");
            listLine.add("ex.printStackTrace();");
            listLine
                    .add("throw new IllegalArgumentException(\""
                            + processStructure.getName()
                            + ": Exception occurs during processing the file [\" + fileMeta[index].getAbsolutePath() + \"]. \" + ex.toString());");
            listLine.add("}");
            listLine.add("}");
            listLine.add("");
            listLine.add("if (fCacheMeta2Xml) {");
            listLine
                    .add("System.out.println(\"m2x: cache: \" + fCacheMeta2XmlCount + \" file skipped.\");");
            listLine.add("}");
            listLine
                    .add("final long costMills = System.currentTimeMillis() - startMills + 1;");
            listLine
                    .add("System.out.println(\"m2x: end: \" + (costMills / 1000) + \" sec, \" + totalFileCount + \" file, \" + totalFileBytes + \" byte (\" + (totalFileBytes * 1000 / costMills) + \" byte/sec).\");");
        }

        {
            final BlancoCgMethod methodProgress = fCgFactory.createMethod(
                    "progress", "�����̐i���������܂��B");
            fCgClass.getMethodList().add(methodProgress);
            methodProgress.setAccess("protected");
            methodProgress.getLangDoc().getDescriptionList().add(
                    "�i���\�������������ꍇ�ɂ͌p�����ď�������肱�݂܂��B");
            methodProgress.getParameterList().add(
                    fCgFactory.createParameter("progressCurrent", "int",
                            "���ݏ������Ă��錏���̔ԍ��B"));
            methodProgress.getParameterList().add(
                    fCgFactory
                            .createParameter("progressTotal", "int", "�����������B"));
            methodProgress.getParameterList().add(
                    fCgFactory.createParameter("progressItem",
                            "java.lang.String", "�������Ă���A�C�e�����B"));
            methodProgress.setReturn(fCgFactory.createReturn("boolean",
                    "�����𑱍s���Ă悢���ǂ����Bfalse�Ȃ珈�����f�B"));
            final List<java.lang.String> listLine = methodProgress
                    .getLineList();

            listLine.add("// ��ɏ������s������ true ��߂��܂��B");
            listLine.add("return true;");
        }

        BlancoCgTransformerFactory.getJavaSourceTransformer().transform(
                fCgSourceFile, fileBlancoMain);
    }
}
