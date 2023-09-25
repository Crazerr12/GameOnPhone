package com.example.gameonphone.presentation.game

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import com.example.gameonphone.databinding.FragmentGameBinding
import com.example.gameonphone.presentation.models.ExtendedPlayer
import com.example.gameonphone.presentation.models.MonsterModel
import com.example.gameonphone.presentation.models.PlayerModel

class GameFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        binding = FragmentGameBinding.inflate(inflater, container, false)

        val player = PlayerModel(currentHealth = 20, maximumHealth = 20, attack = (1..6), armor = 5)
        val monster =
            MonsterModel(currentHealth = 20, maximumHealth = 20, attack = (1..6), armor = 3)
        var numberOfHealing = 4 // Количество исцелений героя

        binding.textHealthPlayer.text = player.maximumHealth.toString()
        binding.textAttackPlayer.text =
            player.attack.first.toString() + "-" + player.attack.last.toString()
        binding.textArmorPlayer.text = player.armor.toString()
        binding.textHealthMonster.text = monster.maximumHealth.toString()
        binding.textAttackMonster.text =
            monster.attack.first.toString() + "-" + monster.attack.last.toString()
        binding.textArmorMonster.text = monster.armor.toString()
        binding.textHeal.text = numberOfHealing.toString()
        
        binding.buttonHeal.setOnClickListener { 
            if (numberOfHealing == 0){
                toastShowShort("Исцеления закончены")
            } else if (player.currentHealth == 0){
                toastShowShort("Вы не можете исцелиться, так как мертвы")
            }
            else {
                ExtendedPlayer().heal(player)
                numberOfHealing--
                binding.textHeal.text = numberOfHealing.toString()
                binding.textHealthPlayer.text = player.currentHealth.toString()
            }
        }

        binding.buttonHit.setOnClickListener {

            /* Пока количество здоровья героя больше 0,
             * игра продолжается */

            if (player.currentHealth > 0) {

                val playerAttack = player.attack.random()
                val monsterAttack = monster.attack.random()

                var modifierOfAttackPlayer: Int = playerAttack - monster.armor + 1
                var modifierOfAttackMonster: Int = monsterAttack - player.armor + 1

                /* Если модификатор урона будет меньше одного,
                 * то мы будем приравнивать его единице,
                 * чтобы кубик был брошен минимум 1 раз */

                if (modifierOfAttackPlayer < 1) modifierOfAttackPlayer = 1
                if (modifierOfAttackMonster < 1) modifierOfAttackMonster = 1

                var playerDiceCounter = 1
                var monsterDiceCounter = 1

                /* Кидаем кубик, пока их количество
                *  не станет равно модификатору атаки*/

                while (playerDiceCounter <= modifierOfAttackPlayer) {
                    val dice =
                        (1..6).random()
                    if (dice == 5 || dice == 6) {
                        toastShowShort("Ваш удар успешен, вы наносите урон в виде $playerAttack")
                        monster.currentHealth -= playerAttack
                        binding.textHealthMonster.text = monster.currentHealth.toString()
                        if (monster.currentHealth <= 0) { //
                            toastShowShort("Монстр умер, но он был не один")
                            monster.maximumHealth += 2
                            monster.currentHealth = monster.maximumHealth
                            binding.textHealthMonster.text =
                                monster.currentHealth.toString()
                        }
                        break // Заканчиваем цикл, если удар успешен
                    } else {
                        toastShowShort("Вы промахнулись")
                        playerDiceCounter++
                    }
                }

                while (monsterDiceCounter <= modifierOfAttackMonster) {
                    val dice = (1..6).random()
                    if (dice == 5 || dice == 6) {
                        toastShowLong("Удар монстра успешен, он наносит урон в виде $monsterAttack")
                        player.currentHealth -= monsterAttack
                        binding.textHealthPlayer.text = player.currentHealth.toString()
                        if (player.currentHealth <= 0) {
                            binding.textHealthPlayer.text = "0"
                            toastShowLong("Ваши умерли")
                        }
                        break
                    } else {
                        toastShowLong("Монстр промахнулся")
                        monsterDiceCounter++
                    }
                }
            } else toastShowLong("Вы мертвы")
        }
        return binding.root
    }

    fun toastShowShort(string: String) {
        Toast.makeText(requireContext(), string, LENGTH_SHORT).show()
    }

    fun toastShowLong(string: String) {
        Toast.makeText(requireContext(), string, LENGTH_LONG).show()
    }
}