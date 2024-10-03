package com.sports.freesportssample.ui.league.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sports.freesportssample.ui.league.list.model.TeamUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SearchTeamsContentView(
    modifier: Modifier,
    teams: ImmutableList<TeamUi>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            count = teams.size,
            key = { index -> teams[index].id }
        ) { index ->
            val team = teams[index]
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AsyncImage(
                    model = team.badge,
                    contentDescription = null,
                )
            }
        }
    }
}