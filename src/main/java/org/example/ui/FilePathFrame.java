package org.example.ui;

import org.example.function.Function;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class FilePathFrame extends JFrame implements Log {
    Function<String,Void> filePathGetFunction;
    private JTextPane jText;
    private StringBuilder builder;

    public FilePathFrame( ) throws HeadlessException {
        builder=new StringBuilder();
        setUI();
    }

    public void setFilePathGetFunction(Function<String, Void> filePathGetFunction) {
        this.filePathGetFunction = filePathGetFunction;
    }

    private void setUI(){
        setSize(500,500);
        setResizable(false);
        setTitle("自动签名工具");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //JscrollPanne 实现自动滚动


        jText = new JTextPane();
        jText.setEditable(false);
        Font font = new Font("Default", Font.BOLD, 30 );
        jText.setFont(font);

        JScrollPane pane = new JScrollPane(jText);
        pane.setPreferredSize(new Dimension(500,500));
        add(pane,BorderLayout.CENTER);


        jText.setTransferHandler(new TransferHandler(){
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    i(filepath);
                    if(filePathGetFunction!=null){
                        filePathGetFunction.call(filepath);
                    }

                    return true;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                return true;
            }


        });

    }


    @Override
    public void i(String info) {
       insertString(info,Color.BLACK);
    }

    private void insertString(String info,Color color){
        info="\n"+info;
        SimpleAttributeSet   attrSet   =   new SimpleAttributeSet();
        StyleConstants.setForeground(attrSet,   color);
        Document document = jText.getDocument();
        try {
            document.insertString(document.getLength(),info,attrSet);
            //自动滚动到结尾
            jText.setCaretPosition(document.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void e(String info) {
        insertString(info,Color.RED);
    }
}
