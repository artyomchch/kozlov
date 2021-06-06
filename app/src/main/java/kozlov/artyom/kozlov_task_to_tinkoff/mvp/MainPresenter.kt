package kozlov.artyom.kozlov_task_to_tinkoff.mvp

import kozlov.artyom.kozlov_task_to_tinkoff.fragments.lastMVP.LastFragmentInterface
import kozlov.artyom.kozlov_task_to_tinkoff.fragments.lastMVP.LastFragmentModel

class MainPresenter(_view: MainInterface.View): MainInterface.Presenter {
    private var view: MainInterface.View = _view
    private var model: MainInterface.Model = MainModel()
}