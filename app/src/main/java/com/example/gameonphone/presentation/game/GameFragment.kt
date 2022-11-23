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
import com.example.gameonphone.presentation.models.MonsterModel
import com.example.gameonphone.presentation.models.PlayerModel

class GameFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", MODE_PRIVATE)
        binding = FragmentGameBinding.inflate(inflater, container, false)

        var playerModel = PlayerModel(health = 20, attack = (1..6), armor = 5)
        val monsterModel = MonsterModel(health = 20, attack = (1..6), armor = 3)

        var player: PlayerModel = PlayerModel(health = 20, attack = (1..6), armor = 5)
        var monster: MonsterModel = MonsterModel(health = 20, attack = (1..6), armor = 3)
        var counter = 3 // Количество исцелений героя

        binding.textHealthPlayer.text = player.health.toString()
        binding.textAttackPlayer.text =
            player.attack.first.toString() + "-" + player.attack.last.toString()
        binding.textArmorPlayer.text = player.armor.toString()
        binding.textHealthMonster.text = monster.health.toString()
        binding.textAttackMonster.text =
            monster.attack.first.toString() + "-" + monster.attack.last.toString()
        binding.textArmorMonster.text = monster.armor.toString()


        binding.buttonHit.setOnClickListener {
            if (counter > 0) {      //Когда количество исцелений героя становится равно нулю, то игра заканчивается

                val playerAttack = player.attack.random()
                val monsterAttack = monster.attack.random()

                var modifierOfAttackPlayer: Int = playerAttack - monster.armor + 1
                if (modifierOfAttackPlayer < 0)    //Если модификатор урона будет меньше нуля, то мы
                    modifierOfAttackPlayer =
                        0     //будем приравнивать его нулю, чтобы кубик был брошен минимум 1 раз

                var modifierOfAttackMonster: Int = monsterAttack - player.armor + 1
                if (modifierOfAttackMonster < 0)   //Если модификатор урона будет меньше нуля, то мы
                    modifierOfAttackMonster =
                        0    //будем приравнивать его нулю, чтобы кубик был брошен минимум 1 раз

                val dice = (1..6).random()
                var i = 0
                var k = 0

                // в последующих циклах мы будем проверять успех удара
                while (i <= modifierOfAttackPlayer) {  // так как количество кубиков совпадает с модификатором атаки,
                    if (dice == 5 || dice == 6) {      // то в этот цикл будет работать, пока не станет равен модификатору
                        toastShowShort("Ваш удар успешен, вы наносите урон в виде $playerAttack")
                        monster.health -= playerAttack
                        binding.textHealthMonster.text = monster.health.toString()
                        if (monster.health <= 0) {    // Если монстр умирает, то будет появляться новый, сильнее прежнего
                            toastShowShort("Монстр умер, но он был не один")
                            monster = monsterModel.copy(
                                health = monsterModel.health + 2,
                                attack = monsterModel.attack,
                                armor = monsterModel.armor
                            )
                            monsterModel.health + 2
                            binding.textHealthMonster.text =
                                monster.health.toString()  // Меняем количество здоровья у монстра
                        }
                        break           // Здесь, если удар успешен, мы заканчиваем цикл
                    } else if (k == modifierOfAttackMonster) {
                        toastShowShort("Вы промахнулись")
                        i++
                    } else i++
                }

                while (k <= modifierOfAttackMonster) {
                    if (dice == 5 || dice == 6) {
                        toastShowLong("Удар монстра успешен, он наносит урон в виде $monsterAttack")
                        player.health -= monsterAttack
                        binding.textHealthPlayer.text = player.health.toString()
                        if (player.health <= 0) {
                            counter--
                            when (counter) {
                                2, 1 -> {
                                    toastShowLong("Вы умерли, у вас осталось $counter возрождений")
                                    player = playerModel.copy(
                                        health = playerModel.health / 2,
                                        attack = playerModel.attack,
                                        armor = playerModel.armor
                                    )
                                    binding.textHealthPlayer.text = player.health.toString()
                                }
                                0 -> {
                                    toastShowLong("Ваши жизни закончились, GAME OVER")
                                    break
                                }
                            }
                        }
                        break           // Здесь, если удар успешен, мы заканчиваем цикл
                    } else if (k == modifierOfAttackMonster) {
                        toastShowLong("Монстр промахнулся")
                        k++
                    } else k++
                }

            } else toastShowLong("Ваши жизни закончились") // Если количество жизней равно 0, то игра не будет работать
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