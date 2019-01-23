package monopoly.contenido.avatares;

import monopoly.contenido.Jugador;
import monopoly.contenido.casillas.Casilla;
import monopoly.contenido.casillas.propiedades.Propiedades;
import monopoly.excepciones.comandos.ExcepcionNumeroPartesComando;
import monopoly.excepciones.dinero.ExcepcionDineroDeuda;
import monopoly.excepciones.dinero.ExcepcionDineroVoluntario;
import monopoly.excepciones.restricciones.ExcepcionRestriccionComprar;
import monopoly.excepciones.restricciones.ExcepcionRestriccionEdificar;
import monopoly.excepciones.restricciones.ExcepcionRestriccionHipotecar;
import monopoly.plataforma.Operacion;
import monopoly.plataforma.Juego;
import monopoly.plataforma.Tablero;
import monopoly.plataforma.Valor;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public final class Sombrero extends Avatar{ //Las clases hoja de una jerarquía deberían ser finales

    private double historialAlquileres; //Revisa
    private double historialCompras;
    private ArrayList<Propiedades> historialCompradas;
    private double historialSalida;
    private double historialPremios;
    private double historialImpuestos;

    public String getTipo(){ return "Sombrero";}

    public Sombrero(Jugador jug, Tablero tablero,BufferedImage ficha){
        super(jug,tablero, ficha);
        this.historialCompradas = new ArrayList<>();
        super.numTiradas = 3;
    }

    public void setNumTiradas(int tiradas){
        if(tiradas >= 0)
            super.numTiradas = tiradas;
    }

    public double getHistorialCompras() {
        return this.historialCompras;
    }

    public void setHistorialAlquileres(double alquiler){
        if(alquiler>0) this.historialAlquileres=alquiler;
    }

    public void modificarHistorialCompras(double valor){
        this.historialCompras += valor;
    }

    public void modificarHistorialImpuestos(double valor){
        this.historialImpuestos += valor;
    }

    public void modificarHistorialAlquileres(double valor){
        this.historialAlquileres += valor;
    }

    public void modificarHistorialSalida(double valor){
        this.historialSalida += valor;
    }

    public void modificarHistorialPremios(double valor){
        this.historialPremios += valor;
    }

    public void anhadirHistorialCompradas(Propiedades p){
        this.historialCompradas.add(p);
    }

    public void resetHistorial(){
        this.historialAlquileres=0; //Revisa
        this.historialCompras=0;
        this.historialSalida=0;
        this.historialImpuestos = 0;
        this.historialPremios = 0;
        this.historialCompradas.clear();
    }

    public void moverEnAvanzado(int valor) throws ExcepcionRestriccionHipotecar, ExcepcionDineroDeuda, ExcepcionRestriccionEdificar, ExcepcionDineroVoluntario, ExcepcionRestriccionComprar {
        Operacion operacion = new Operacion(super.getTablero());
        if(valor > 4){
            this.resetHistorial();
            this.moverZigZag(valor);
            this.getCasilla().accionCaer(this.getJugador(), valor, operacion);
        }else {
            if(super.numTiradas==3){
                this.deshacerHistorial();
            }
            super.numTiradas = 0;
            Juego.consola.anhadirTexto("El sombrero ya ha acabado sus tiradas este turno");
        }
    }

    private void moverACasilla(int valor){
        this.getCasilla().quitarAvatar(this);
        this.setCasilla(Valor.casillas.get(valor));
        this.getCasilla().anhadirAvatar(this);
    }

    private void actualizarVueltaAvanzado(){
        this.jugador.modificarDinero(Valor.getDineroVuelta());
        this.jugador.modificarPasarPorCasilla(Valor.getDineroVuelta());
        this.numVueltas++;
        Juego.consola.anhadirTexto("El jugador " + this.jugador.getNombre() + " recibe " + Valor.getDineroVuelta() + "€ por haber cruzado la salida.");
        //Se recorren los avatares para comprobar si es necesario actualizar el dinero de pasar por la casilla de salida
        Iterator<Avatar> avatar_i = this.tablero.getAvatares().values().iterator();
        while(avatar_i.hasNext()) {
            Avatar avatar = avatar_i.next();
            if(avatar.numVueltas <= this.tablero.getVueltas() + 3) {
                return;
            }
        }
        this.tablero.modificarVueltas(4);
        Valor.actualizarVuelta();
        this.historialSalida += Valor.getDineroVuelta();
    }

    private void moverZigZag(int valor) {
        if (this.getCasilla().getPosicion() > 0 && this.getCasilla().getPosicion() < 10){
            this.moverACasilla(11);
            if(valor % 2 == 0)
                this.moverACasilla(10 + ((valor - 1 + this.getCasilla().getPosicion() - 10) % 10));
            else
                this.moverACasilla(39 - ((valor - 2 + this.getCasilla().getPosicion() - 10) % 10));
        } else if (this.getCasilla().getPosicion() > 0 && this.getCasilla().getPosicion() < 20) {
            if(valor % 2 == 0)
                this.moverACasilla(10 + ((valor + this.getCasilla().getPosicion() - 10) % 10));
            else
                this.moverACasilla(39 - ((valor + 1 + this.getCasilla().getPosicion() - 10) % 10));
        } else if (this.getCasilla().getPosicion() > 0 && this.getCasilla().getPosicion() < 30) {
            this.moverACasilla(31);
            if(valor + this.getCasilla().getPosicion() > 39){
                this.actualizarVueltaAvanzado();
            }
            if(valor % 2 == 0)
                this.moverACasilla(30 + ((valor - 1 + this.getCasilla().getPosicion() - 30) % 10));
            else
                this.moverACasilla(20 - ((valor - 1 + this.getCasilla().getPosicion() - 30 ) % 10));
        } else {
            Casilla casillaEste = this.getCasilla();
            if(this.getCasilla().getPosicion() == 0)
                casillaEste = tablero.getCasillas().get("VeCarcel");
            if(valor % 2 == 0)
                this.moverACasilla(30 + ((valor + casillaEste.getPosicion() - 30) % 10));
            else
                this.moverACasilla(20 - ((valor + casillaEste.getPosicion() - 30 ) % 10));
        }
    }

    private void deshacerHistorial(){
        Juego.consola.anhadirTexto("Se desharán las acciones realizadas en la tirada anterior:");
        if(this.historialAlquileres>0) {
            super.getJugador().modificarDinero(this.historialAlquileres);
            super.getJugador().modificarPagoAlquileres(-this.historialAlquileres);
            Juego.consola.anhadirTexto("Se ha deshecho la accion pagar alquiler.");
            Juego.consola.anhadirTexto("Recuperas "+this.historialAlquileres+ ", tu fortuna aumenta a "+super.getJugador().getDinero());
        }
        if(this.historialSalida>0) {
            super.getJugador().modificarDinero(-this.historialSalida);
            super.getJugador().modificarPasarPorCasilla(-this.historialSalida);
            Juego.consola.anhadirTexto("Se ha deshecho la accion pasar por la casilla de salida.");
            Juego.consola.anhadirTexto("Pierdes "+ this.historialSalida+ ", tu fortuna se reduce a "+super.getJugador().getDinero());
        }
        if(this.historialImpuestos>0) {
            super.getJugador().modificarDinero(this.historialImpuestos);
            super.getJugador().modificarPagoImpuestos(-this.historialImpuestos);
            Juego.consola.anhadirTexto("Se ha deshecho la accion pagar impuesto.");
            Juego.consola.anhadirTexto("Recuperas "+this.historialImpuestos+ ", tu fortuna aumenta a "+super.getJugador().getDinero());
        }
        if(this.historialCompradas.size() >= 1){
            for(Propiedades p: this.historialCompradas) {
                super.getJugador().borrarPropiedad(p);
                Juego.consola.anhadirTexto("Se ha retirado la casilla " +  p.getNombre());
            }
            super.getJugador().modificarDinero(this.historialCompras);
            Juego.consola.anhadirTexto("Se han devuelto "  + this.historialCompras + "€.");
        }

        if(this.historialPremios != 0){
            if(this.historialPremios > 0) {
                super.getJugador().modificarDinero(-this.historialPremios);
                super.getJugador().modificarPremiosInversionesOBote(-this.historialPremios);
                Juego.consola.anhadirTexto("Se ha deshecho la accion pagar premio.");
                Juego.consola.anhadirTexto("Pierdes " + this.historialPremios + ", tu fortuna disminuie en " + super.getJugador().getDinero());
            }else{
                super.getJugador().modificarDinero(-this.historialPremios);
                super.getJugador().modificarPremiosInversionesOBote(-this.historialPremios);
                Juego.consola.anhadirTexto("Se ha deshecho la accion cobrar premio.");
                Juego.consola.anhadirTexto("Recuperas " + this.historialPremios + ", tu fortuna aumenta a " + super.getJugador().getDinero());

            }
        }

        resetHistorial();
    }
}
