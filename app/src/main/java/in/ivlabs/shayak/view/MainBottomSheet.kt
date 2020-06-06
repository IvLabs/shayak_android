package `in`.ivlabs.shayak.view

import `in`.ivlabs.shayak.storage.RobotStorageInterface
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.ivlabs.shayak.R


class MainBottomSheet(val presenter: MainActivityPresenter) : BottomSheetDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SheetStyle)

    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.main_add_bottom_sheet, null)
        dialog.setContentView(contentView)
        val uuidInput = contentView.findViewById<TextInputEditText>(R.id.main_add_uuid_input)
        val nameInput = contentView.findViewById<TextInputEditText>(R.id.main_add_name_input)
        val descriptionInput =
            contentView.findViewById<TextInputEditText>(R.id.main_add_description_input)
        val conformButton =
            contentView.findViewById<ExtendedFloatingActionButton>(R.id.main_add_conform)
        conformButton.setOnClickListener {
            if (!uuidInput.text.isNullOrBlank() && !nameInput.text.isNullOrBlank() && !descriptionInput.text.isNullOrBlank()) {
                val robot = RobotStorageInterface.RobotData(
                    uuidInput.text.toString(),
                    nameInput.text.toString(),
                    descriptionInput.text.toString(),
                    false
                )
                presenter.storeRobot(robot)
            }
        }

    }

}