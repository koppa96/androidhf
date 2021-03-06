package hu.aut.bme.androidchatter

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.aut.bme.androidchatter.adapters.MessageAdapter
import hu.aut.bme.androidchatter.adapters.UserAdapter
import hu.aut.bme.androidchatter.models.Chat
import hu.aut.bme.androidchatter.models.Message
import hu.aut.bme.androidchatter.models.User
import kotlinx.android.synthetic.main.activity_chat.*
import hu.aut.bme.androidchatter.databinding.ActivityChatBinding
import hu.aut.bme.androidchatter.fragments.SettingsFragment
import hu.aut.bme.androidchatter.viewmodels.ChatViewModel

class ChatActivity : AppCompatActivity() {
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        when (prefs.getString(SettingsFragment.THEME_KEY, SettingsFragment.THEME_DARK)) {
            SettingsFragment.THEME_DARK -> setTheme(R.style.AppThemeActionbar)
            SettingsFragment.THEME_LIGHT -> setTheme(R.style.AppThemeActionbarLight)
        }

        super.onCreate(savedInstanceState)

        val chat = intent.getSerializableExtra("chat") as Chat

        val binding = DataBindingUtil.setContentView<ActivityChatBinding>(this, R.layout.activity_chat)
        binding.viewModel = ChatViewModel(chat)

        setSupportActionBar(toolbar)

        val currentUser = FirebaseAuth.getInstance().currentUser!!
        supportActionBar?.title = if (currentUser.uid == chat.user1Id) chat.user2Name else chat.user1Name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        adapter = binding.viewModel!!.setUpAdapter()
        recyclerView.emptyView = tvEmptyList
        recyclerView.emptyMessage = getString(R.string.no_messages)
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Scroll down on keyboard opening
        recyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed({
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }, 100)
            }
        }

        // Scroll down on data addition
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                recyclerView.postDelayed({
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }, 100)
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                recyclerView.postDelayed({
                    recyclerView.smoothScrollToPosition(adapter.itemCount)
                }, 100)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }
}
