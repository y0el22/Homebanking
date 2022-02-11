package com.mindhub.homebanking.controllers;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.TransactionDTO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PDFExportTransaction {
    private List<TransactionDTO> listaTRansaction;

    public PDFExportTransaction(List<TransactionDTO> listaTRansaction) {
        super();
        this.listaTRansaction = listaTRansaction;
    }
    //Fuentes para las letras
    Font fuente1 = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,Color.DARK_GRAY);
    Font fuente2 = FontFactory.getFont(FontFactory.HELVETICA_BOLD ,14,Color.BLACK);
    Font fuente3 = FontFactory.getFont(FontFactory.HELVETICA ,10,Color.BLACK);
    Font fuente4 = FontFactory.getFont(FontFactory.HELVETICA_BOLD ,12,Color.BLACK);
    //Datos de informacion
    public void encabe(PdfPTable enca){
        PdfPCell cell= new PdfPCell();
        cell.setPadding(2);
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase("DebugWin   BootCamp JAVA EY",fuente1));
        enca.addCell(cell);
        cell.setPhrase(new Phrase("Av. Apoquindo, Providencia 4321",fuente1));
        enca.addCell(cell);
        cell.setPhrase(new Phrase("Soporte@Debugwin.cl        +56987654321",fuente1));
        enca.addCell(cell);

    }
    //Titulo transaccion
    public void transa(PdfPTable tran){
        PdfPCell cell= new PdfPCell();
        cell.setPadding(25);
        cell.setBorder(0);

        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase("Transacciones",fuente2));
        tran.addCell(cell);

    }
    //Encabezado
    public void encabezado(PdfPTable tablaTransac){
        PdfPCell cell= new PdfPCell();
        cell.setBackgroundColor(Color.gray);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.BLACK);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase("ID",fuente4));
        tablaTransac.addCell(cell);
        cell.setPhrase(new Phrase("Descripcion",fuente4));
        tablaTransac.addCell(cell);
        cell.setPhrase(new Phrase("Monto",fuente4));
        tablaTransac.addCell(cell);
        cell.setPhrase(new Phrase("Fecha",fuente4));
        tablaTransac.addCell(cell);
    }
    //Info BD
    public void cuerpo(PdfPTable tablaTransac){
        PdfPCell cell= new PdfPCell();
        cell.setBackgroundColor(Color.lightGray);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        for (TransactionDTO client:listaTRansaction ) {
            cell.setPhrase(new Phrase(client.getId().toString(),fuente3));
            tablaTransac.addCell(cell);
            cell.setPhrase(new Phrase(client.getDescription(),fuente3));
            tablaTransac.addCell(cell);
            cell.setPhrase(new Phrase(String.valueOf(client.getAmount()),fuente3));
            tablaTransac.addCell(cell);
            cell.setPhrase(new Phrase(client.getDate().toString(),fuente3));
            tablaTransac.addCell(cell);
        }

    }
     public void exportTransc (HttpServletResponse response)throws IOException {
        //Creando la hoja del PDF
         Document document=new Document(PageSize.LETTER);
         PdfWriter.getInstance(document, response.getOutputStream());
         document.open();

         //Agregando imagen
         Image imagen = Image.getInstance("src/main/resources/static/web/img/logo vinotinto.png");
         imagen.setAbsolutePosition(80f, 670f);
         imagen.scalePercent(10);

         //Paragraph title=new Paragraph(" DebugWin   BootCamp JAVA EY \n" +
              //   "Av. Apoquindo, providencia 4321 \n Soporte@Debugwin.cl        +56987654321",font);

         //document.add(title);
         //Indicando la posicion de las tablas
         PdfPTable tablaTransac= new PdfPTable(4);
         PdfPTable enca=new PdfPTable(1);
         PdfPTable tran=new PdfPTable(1);
         enca.setWidthPercentage(100);
         enca.setSpacingBefore(15);
         tran.setWidthPercentage(100);
         tran.setSpacingBefore(15);
         tablaTransac.setWidthPercentage(100);
         tablaTransac.setSpacingBefore(15);
         tablaTransac.setWidths(new float[]{1.0f,3.0f,3.0f,4.0f});
         encabe(enca);
         transa(tran);
         encabezado(tablaTransac);
         cuerpo(tablaTransac);
         //Agregando la info del las tablas a la hoja
         document.add(imagen);
         document.add(enca);
         document.add(tran);
         document.add(tablaTransac);



         document.close();

     }
}
