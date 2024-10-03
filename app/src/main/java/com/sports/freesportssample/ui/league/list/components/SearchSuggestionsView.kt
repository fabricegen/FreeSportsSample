package com.sports.freesportssample.ui.league.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sports.freesportssample.ui.league.list.model.LeagueUi
import kotlinx.collections.immutable.ImmutableList

@Composable
fun SearchSuggestionsView(
    modifier: Modifier,
    filteredItems: ImmutableList<LeagueUi>,
    onLeagueSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(
            count = filteredItems.size,
            key = { index -> filteredItems[index].id }
        ) { index ->
            val league = filteredItems[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onLeagueSelected(league.name)
                    },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(modifier = Modifier.padding(16.dp), text = league.name)
            }
        }
    }
}