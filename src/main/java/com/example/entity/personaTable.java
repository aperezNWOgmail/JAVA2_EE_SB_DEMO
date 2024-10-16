/**
 *      public int ID_column { get; set; }
        public string NombreCompleto { get; set; }
        public string ProfesionOficio { get; set; }
        public string Ciudad { get; set; }
*/
package com.example.entity;

public class personaTable {
    //
    private long id_Column;
    private String nombreCompleto;
    private String profesionOficio;
    private String ciudad;

    public personaTable(long id_Column, String nombreCompleto, String profesionOficio,String ciudad) {
        this.id_Column       = id_Column;
        this.nombreCompleto  = nombreCompleto;
        this.profesionOficio = profesionOficio;
        this.ciudad          = ciudad;
    }

    public long getId_Column() {
        return id_Column;
    }

    public void setId_Column(long id_Column) {
        this.id_Column = id_Column;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getProfesionOficio() {
        return profesionOficio;
    }

    public void setProfesionOficio(String profesionOficio) {
        this.profesionOficio = profesionOficio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
