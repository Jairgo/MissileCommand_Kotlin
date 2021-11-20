package com.example.missilecommand_kotlin

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.media.SoundPool
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.*


class Board(context: Context?) : SurfaceView(context), Runnable {
    private val surfaceHolder: SurfaceHolder

    @Volatile
    private var drawing = true
    private var thread: Thread? = null
    var dimensions: RectF? = null
        private set
    private var canvas: Canvas? = null

    // Arrays de bombas, misiles y ciudades
    private val bombs: ArrayList<Bomb> = ArrayList<Bomb>()
    private val missiles: ArrayList<Missile> = ArrayList<Missile>()
    private val cities: ArrayList<City> = ArrayList<City>()
    private var battery: Battery? = null
    private var gameOver = false

    protected var timerValue = -1

    private val expId: Int
    private val expBombId: Int
    private var countMissiles = 0
    private var hilo: Thread? = null

    // Posiciones en X y Y de las bombas y los misiles
    var misPosX = 0f
    var misPosY = 0f
    var bombPosX = 0f
    var bombPosY = 0f
    private val explosionSp: SoundPool
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action and MotionEvent.ACTION_MASK == MotionEvent.ACTION_DOWN) bombs.add(
            Bomb(
                PointF(event.x, event.y)
            )
        )
        // Log.i("BoardClass", "Se creó una nueva bomba y se agrego al array list");
        return true
    }

    override fun run() {
        while (drawing) {
            update()
            draw()
            checkBomb()
            checkCities()
        }
    }

    fun setDimensions(left: Float, top: Float, right: Float, bottom: Float) {
        dimensions = RectF(left, top, right, bottom)
    }

    /**
     * Funcion para añadir las ciudades al board
     * @param dimensions Parametro de las dimensiones de la pantalla, que ocupará para calcular donde colocar las ciudades
     */
    fun addCities(dimensions: RectF) {
        // Obtengo la mitad de la mitad de la pantalla
        val mitad = (dimensions.left + dimensions.right) / 2 / 2

        // Añado las ciudades al array de ciudades
        cities.add(City(dimensions, this.dimensions!!.left + 100))
        cities.add(City(dimensions, this.dimensions!!.left + mitad))
        cities.add(City(dimensions, this.dimensions!!.right - mitad))
        cities.add(City(dimensions, this.dimensions!!.right - 100))

        // Creo un objeto de la clase Battery
        battery = Battery(dimensions, (this.dimensions!!.left + this.dimensions!!.right) / 2)
    }

    /**
     * Función que checa si alguna de las bombas existentes ya llegó a su tamaño maximo
     * Tambien checa si existe una colisión de una bomba con algun misil
     * Los dos For que puse, no tienen el mejor rendimiento, pues esta recorriendo ambos arrays constantemente
     * Se debería bajar la complejidad, buscando otra forma para encontrar la colission
     */
    private fun checkBomb() {
        var eliminado = false

        // Recorre todas las bombas que aun existen
        for (i in bombs.indices) {
            if (!missiles.isEmpty()) {
                // Recorre todos las misiles que aun existen
                for (j in missiles.indices) {
                    misPosX = missiles[j].center.x
                    misPosY = missiles[j].center.y

                    bombPosX = bombs[i].center.x
                    bombPosY = bombs[i].center.y
                    val a = Math.pow((misPosX - bombPosX).toDouble(), 2.0)
                        .toFloat()
                    val b = Math.pow((misPosY - bombPosY).toDouble(), 2.0)
                        .toFloat()

                    // Distancia que existe entre la bomba y el misil
                    val distancia = Math.sqrt((a + b).toDouble()).toFloat()
                    if (distancia <= bombs[i].radius) {
                        bombs.removeAt(i)
                        missiles.removeAt(j)
                        eliminado = true
                        explosionSp.play(expBombId, 1f, 1f, 0, 0, 1f)
                        countMissiles++
                        break
                    }
                }
            }

            // Solo entra aquí si la bomba que quiero revisar no ha sido eliminada ya
            if (!eliminado) {
                if (bombs[i].radius >= bombs[i].maxSize) {
                    bombs.removeAt(i)
                    // Log.i("BoardClass", "Se eliminó la bomba en la posición " + i);
                }
                eliminado = false
            }
        }
    }

    private fun update() {
        val runnable = Runnable {
            val randTimer = Random()
            val `val` = randTimer.nextInt(8000 + 2000) + 2000
            timerValue = `val`
            try {
                Thread.sleep(`val`.toLong())
                // Log.i("Hilo", "" + Thread.currentThread().getName());
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            missiles.add(Missile(dimensions!!, cities))
            timerValue = -1
        }

        // Hilo para ir creando los misiles cada cierto tiempo random
        if (timerValue == -1) {
            hilo = Thread(runnable)
            hilo!!.start()
        }
        for (i in bombs.indices) bombs[i].update(dimensions)
        for (i in missiles.indices) {
            // Log.i("UpdateMissile", "" + i);
            missiles[i].update(dimensions)

            // Aquí se evalua si el misil colisionó con una ciudad, si es así, le cambia el
            // valor de setAlive y reproduce el sonido
            val cityIndex: Int = missiles[i].detectCollisionCity()
            if (cityIndex != -1) {
                if (!cities.isEmpty() && cityIndex <= cities.size) {
                    if (cities[cityIndex].isAlive)
                        explosionSp.play(expId, 1f, 1f, 0, 0, 1f)
                    cities[cityIndex].isAlive = false
                }
                missiles.removeAt(i)
                break
            }
        }
    }

    private fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()

            // Limpio la pantalla
            canvas?.drawColor(Color.argb(255, 0, 0, 0))

            // Dibujo la batería
            battery!!.draw(canvas)

            // Dibujo las bombas que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for (i in bombs.indices) bombs[i].draw(canvas)

            // Dibujo los misiles que esten disponibles, que aun no hayan sido eliminadas del arraylist
            for (i in missiles.indices) missiles[i].draw(canvas)
            if (!gameOver) {
                // Dibujo las ciudades que estan disponibles, que aun no hay sido eliminadas del array list
                for (i in cities.indices) {
                    if (cities[i].isAlive) cities[i].draw(canvas)
                }
            } else {
                // Si ya se eliminaron todas las ciudad el juego termina.
                drawing = false
                canvas?.drawColor(Color.argb(255, 255, 255, 255))
                val myIntent = Intent(context, EndGameActivity::class.java)
                myIntent.putExtra("missiles", countMissiles.toString())
                context.startActivity(myIntent)
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    /**
     * Funcion que va checando cuantas ciudades ya no están vivas.
     * Si encuentra que todas las ciudades ya no están vivas, termina el juego
     */
    fun checkCities() {
        var count = 0
        for (i in cities.indices) {
            if (!cities[i].isAlive) count++
        }
        if (count > 3) {
            gameOver = true
            bombs.clear()
            cities.clear()
            missiles.clear()
        }
    }

    fun resume() {
        drawing = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause() {
        drawing = false
        try {
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    init {
        surfaceHolder = holder
        explosionSp = SoundPool.Builder().build()
        expId = explosionSp.load(context, R.raw.explosion, 1)
        expBombId = explosionSp.load(context, R.raw.explosion_bomb, 1)
    }
}