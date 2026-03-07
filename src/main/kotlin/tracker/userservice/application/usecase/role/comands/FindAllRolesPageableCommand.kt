package tracker.userservice.application.usecase.role.comands

data class FindAllRolesPageableCommand(
    val pageAmount: Int,
    val size: Int
)