package aschi2403.tsiy.helper

import android.content.Context
import com.maltaisn.icondialog.pack.IconPack
import com.maltaisn.icondialog.pack.IconPackLoader
import com.maltaisn.iconpack.defaultpack.createDefaultIconPack

class IconPackProvider(var context: Context) {

    fun loadIconPack(): IconPack {
        val loader = IconPackLoader(context)

        val iconPack = createDefaultIconPack(loader)
        iconPack.loadDrawables(loader.drawableLoader)
        return iconPack
    }
}
