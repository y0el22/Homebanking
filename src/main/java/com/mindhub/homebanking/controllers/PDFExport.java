package com.mindhub.homebanking.controllers;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.homebanking.dtos.ClientDTO;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class PDFExport  {
    private List<ClientDTO> listaCliente;

    public PDFExport(List<ClientDTO> listaCliente) {
        super();
        this.listaCliente = listaCliente;
    }
    private void writeTableHeader(PdfPTable tablaClientes){
        PdfPCell cell= new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
        cell.setPhrase(new Phrase("Cliente ID"));
        tablaClientes.addCell(cell);
        cell.setPhrase(new Phrase("Nombre"));
        tablaClientes.addCell(cell);
        cell.setPhrase(new Phrase("Apellido"));
        tablaClientes.addCell(cell);
        cell.setPhrase(new Phrase("Email"));
        tablaClientes.addCell(cell);
    }
    private void writeTable(PdfPTable tablaClientes){
        for (ClientDTO client:listaCliente) {
            tablaClientes.addCell(client.getId().toString());
            tablaClientes.addCell(client.getFirstName());
            tablaClientes.addCell(client.getLastName());
            tablaClientes.addCell(client.getEmail());

        }

    }
    public void export(HttpServletResponse response) throws IOException {
        Document document=new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.BLUE);
        font.setSize(18);

        Paragraph title=new Paragraph("Lista de clientes",font);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        PdfPTable tablaClientes= new PdfPTable(4);
        tablaClientes.setWidthPercentage(100);
        tablaClientes.setSpacingBefore(15);
        tablaClientes.setWidths(new float[]{1.0f,3.0f,3.0f,4.0f});
        writeTableHeader(tablaClientes);
        writeTable(tablaClientes);
        document.add(tablaClientes);



        document.close();

    }

}
