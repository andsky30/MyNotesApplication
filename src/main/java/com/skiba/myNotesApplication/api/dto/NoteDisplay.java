package com.skiba.myNotesApplication.api.dto;

public class NoteDisplay {

    private Long id;
    private String text;

    public NoteDisplay(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public NoteDisplay(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteDisplay that = (NoteDisplay) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NoteDisplay{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
