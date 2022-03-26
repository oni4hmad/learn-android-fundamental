package com.dicoding.picodiploma.myunittest

class MainViewModel(private val cuboidModel: CuboidModel) {
    fun getCircumference(): Double = cuboidModel.getCircumference()
    fun getSurfaceArea(): Double = cuboidModel.getSurfaceArea()
    fun getVolume(): Double = cuboidModel.getVolume()
    fun save(w: Double, l: Double, h: Double) {
        cuboidModel.save(w, l, h)
    }
}