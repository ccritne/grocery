package com.example.grocery.uielements.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.example.grocery.App
import com.example.grocery.utilities.fromAppToGoogle
import com.example.grocery.utilities.getFormatterDateSql
import java.util.Date


@Composable
fun Drawer(
    app: App,
    drawerValues: DrawerValues
){

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.5f),
        drawerShape = RectangleShape
    ) {
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Grocery",
            onlyText = true,
            onClick = {}
        )
        HorizontalDivider()
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Recreate db",
            onlyText = false,
            onClick = { app.dbManager.fillDataWeek() }
        )
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Clean db",
            onlyText = false,
            onClick = { app.dbManager.recreateDb() }
        )
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Clean plans",
            onlyText = false,
            onClick = { app.dbManager.deletePlan() }
        )
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Export week",
            onlyText = false,
            onClick = { fromAppToGoogle(app.unitsMap.value, getFormatterDateSql().parse("2024/07/08")!!, getFormatterDateSql().parse("2024/07/14")!!, app.dbManager) }
        )
    }

}