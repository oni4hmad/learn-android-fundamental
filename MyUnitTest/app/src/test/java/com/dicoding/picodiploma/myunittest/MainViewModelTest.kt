package com.dicoding.picodiploma.myunittest

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.*

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var cuboidModel: CuboidModel

    private val dummyLength = 12.0
    private val dummyWidth = 7.0
    private val dummyHeight = 6.0

    private val dummyVolume = 504.0
    private val dummyCircumference = 100.0
    private val dummySurfaceArea = 396.0

    @Before /* <- annotation */
    /* dijalankan dulu sebelum method dg anotasi @Test,
    *   sdgkan @After dijalankan setelah @Test */
    fun before() {
        cuboidModel = mock(CuboidModel::class.java) /* mock: membuat obyek mock yang akan menggantikan obyek yang asli. */
        mainViewModel = MainViewModel(cuboidModel)
    }

    /* test biasa (tanpa mockito) -- dg JUnit saja */

    @Test
    /* @Test -> digunakan pada method yg akan dites */
    fun testVolume() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth, dummyLength, dummyHeight)
        val volume = mainViewModel.getVolume()
        assertEquals(dummyVolume, volume, 0.0001)
    }

    @Test
    fun testCircumference() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth, dummyLength, dummyHeight)
        val result = mainViewModel.getCircumference()
        assertEquals(dummyCircumference, result, 0.0001)
    }

    @Test
    fun testSurfaceArea() {
        cuboidModel = CuboidModel()
        mainViewModel = MainViewModel(cuboidModel)
        mainViewModel.save(dummyWidth, dummyLength, dummyHeight)
        val result = mainViewModel.getSurfaceArea()
        assertEquals(dummySurfaceArea, result, 0.0001)
    }

    /* test dengan JUnit + Mockito
    *   JUnit -> melakukan unit test
    *   Mockito -> melakukan mock object
    *       fungsi mock object: mereplika objek yang digunakan oleh object yang sedang di-test */

    @Test
    fun testMockVolume() {
        `when`(mainViewModel.getVolume()).thenReturn(dummyVolume)
            /* when() -> untuk menandakan event di mana Anda ingin memanipulasi behavior dari mock object. */
            /* thenReturn() -> digunakan untuk memanipulasi output dari mock object. */
        val volume = mainViewModel.getVolume() /* ini selalu akan mereturn dummyVolume karena mock object sdh dimanipulasi resultnya */
        println("termanipulasi $volume")
        val anu = verify(cuboidModel).getVolume() /* memastikan bahwa cuboidModel.getVolume() dipanggil sekali/times(1) */
            /* verify() -> digunakan untuk memeriksa metode dipanggil dengan arguman yang diberikan. (lib dari Mockito) */
        assertEquals(dummyVolume, volume, 0.0001)
    }
    @Test
    fun testMockCircumference() {
        `when`(mainViewModel.getCircumference()).thenReturn(dummyCircumference)
        val volume = mainViewModel.getCircumference()
        verify(cuboidModel).getCircumference()
        assertEquals(dummyCircumference, volume, 0.0001)
    }
    @Test
    fun testMockSurfaceArea() {
        `when`(mainViewModel.getSurfaceArea()).thenReturn(dummySurfaceArea)
        val volume = mainViewModel.getSurfaceArea()
        verify(cuboidModel).getSurfaceArea()
        assertEquals(dummySurfaceArea, volume, 0.0001)
    }
}